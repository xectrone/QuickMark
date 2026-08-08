package com.xectrone.quickmark.domain.file_handling

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import com.xectrone.quickmark.data.DataStore

/**
 * Central manager for all SAF (Storage Access Framework) permission lifecycle operations.
 *
 * No other part of the app should directly call [android.content.ContentResolver.takePersistableUriPermission],
 * [android.content.ContentResolver.releasePersistableUriPermission], or manipulate the stored
 * directory URI in SharedPreferences. All such operations must go through this object.
 */
object SAFStorageManager {

    private const val TAG = "SAFStorageManager"

    // -----------------------------------------------------------------------------------------
    // URI persistence
    // -----------------------------------------------------------------------------------------

    /** Persist [uri] as the chosen notes directory. */
    fun saveDirectoryUri(context: Context, uri: Uri) {
        DataStore.saveSelectedDirectoryUri(context, uri)
    }

    /** Return the stored directory URI, or `null` if none has been chosen yet. */
    fun getDirectoryUri(context: Context): Uri? {
        return DataStore.getSavedDirectoryUri(context)
    }

    /** Erase the stored directory URI from SharedPreferences. */
    fun clearDirectoryUri(context: Context) {
        DataStore.clearDirectoryUri(context)
    }

    // -----------------------------------------------------------------------------------------
    // Permission management
    // -----------------------------------------------------------------------------------------

    /**
     * Check whether a valid read+write persistable permission covering [uri] is still held.
     *
     * Android records the permission against the **tree URI**
     * (e.g. `…/tree/primary%3ANotes`), while the app stores a **children URI**
     * (e.g. `…/tree/primary%3ANotes/document/primary%3ANotes/children`).
     * The children URI always starts with the tree URI, so we use a startsWith check.
     */
    fun isPermissionPersisted(context: Context, uri: Uri): Boolean {
        return try {
            val uriString = uri.toString()
            context.contentResolver.persistedUriPermissions.any { perm ->
                perm.isReadPermission &&
                    perm.isWritePermission &&
                    uriString.startsWith(perm.uri.toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, "isPermissionPersisted: error checking permissions", e)
            false
        }
    }

    /**
     * Release the persisted permission for [uri].
     * Swallows all exceptions — Android may have already revoked the permission.
     */
    fun releasePermission(context: Context, uri: Uri) {
        try {
            val treeUri = deriveTreeUri(uri)
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            context.contentResolver.releasePersistableUriPermission(treeUri, flags)
            Log.d(TAG, "releasePermission: released permission for $treeUri")
        } catch (e: Exception) {
            // Permission may have already been removed by Android — safe to ignore
            Log.w(TAG, "releasePermission: could not release (may already be gone)", e)
        }
    }

    // -----------------------------------------------------------------------------------------
    // Directory accessibility
    // -----------------------------------------------------------------------------------------

    /**
     * Verify the directory behind [uri] is actually queryable right now.
     *
     * Performs a real SAF query (equivalent to opening the folder). Returns `false` on any
     * failure, including [SecurityException], [IllegalArgumentException],
     * [java.io.FileNotFoundException], and generic [Exception].
     */
    fun isDirectoryAccessible(context: Context, uri: Uri): Boolean {
        return try {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
                null,
                null,
                null
            )
            cursor?.use { true } ?: false
        } catch (e: SecurityException) {
            Log.w(TAG, "isDirectoryAccessible: SecurityException — permission denied", e)
            false
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "isDirectoryAccessible: IllegalArgumentException — bad URI", e)
            false
        } catch (e: java.io.FileNotFoundException) {
            Log.w(TAG, "isDirectoryAccessible: FileNotFoundException — directory gone", e)
            false
        } catch (e: Exception) {
            Log.w(TAG, "isDirectoryAccessible: unexpected error", e)
            false
        }
    }

    /**
     * Full validation: returns `true` only when both the persisted permission exists **and**
     * the directory can actually be queried.
     */
    fun isDirectoryValid(context: Context, uri: Uri): Boolean {
        return isPermissionPersisted(context, uri) && isDirectoryAccessible(context, uri)
    }

    /**
     * Convenience method: validates [uri] and, if invalid, releases the permission and clears
     * the stored URI.
     *
     * @return `true` if the URI is valid and usable; `false` if it was stale and has been cleared.
     */
    fun validateAndClear(context: Context, uri: Uri): Boolean {
        return if (isDirectoryValid(context, uri)) {
            true
        } else {
            Log.w(TAG, "validateAndClear: URI is stale — releasing and clearing")
            releasePermission(context, uri)
            clearDirectoryUri(context)
            false
        }
    }

    // -----------------------------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------------------------

    /**
     * Reconstruct the **tree URI** from a children URI so it can be passed to
     * [android.content.ContentResolver.releasePersistableUriPermission].
     *
     * Android records the grant against `content://<authority>/tree/<treeDocId>`,
     * regardless of whether the stored URI is a children or document URI.
     * [DocumentsContract.getTreeDocumentId] extracts the tree document ID from any
     * tree-rooted URI, letting us rebuild the canonical tree URI.
     */
    private fun deriveTreeUri(uri: Uri): Uri {
        return try {
            val treeDocId = DocumentsContract.getTreeDocumentId(uri)
            val authority = uri.authority ?: return uri
            Uri.parse("content://$authority/tree/${Uri.encode(treeDocId)}")
        } catch (e: Exception) {
            // Fallback: best-effort strip of trailing path segments
            val uriString = uri.toString()
            if (uriString.endsWith("/children")) {
                Uri.parse(uriString.removeSuffix("/children"))
            } else {
                uri
            }
        }
    }
}
