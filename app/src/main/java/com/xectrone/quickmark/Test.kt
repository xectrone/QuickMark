package com.xectrone.quickmark

import android.net.Uri
import android.util.Log
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun main() {
    val uriStr = "content://com.android.externalstorage.documents/tree/primary%3A0.MEDIA%2Fqn%2Fhey/document/primary%3A0.MEDIA%2Fqn%2Fhey%2F2024-06-13T19-41-38.md"

    fun encodeUri(uri: Uri):String {
        return URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8.toString())
    }

    fun decodeUri(encodedUri: String):Uri {
        val decodedString = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString())
        return Uri.parse(decodedString)
    }

    val uriStr2 = decodeUri(encodeUri(Uri.parse(uriStr))).toString()

    Log.d("#TAGMAN", "main: URI 1 : $uriStr")
    Log.d("#TAGMAN", "main: URI 2 : $uriStr2")

}