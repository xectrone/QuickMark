import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.Toast
import com.xectrone.quickmark.R
import com.xectrone.quickmark.ui.add_note_dialog.AddNoteActivity

fun addHomeScreenShortcut(context: Context) {
    // Only available on Android 8.0 (API 26) and above
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        Toast.makeText(context, "Shortcuts not supported on this version", Toast.LENGTH_SHORT).show()
        return
    }

    val shortcutManager = context.getSystemService(ShortcutManager::class.java) ?: return

    if (!shortcutManager.isRequestPinShortcutSupported) {
        Toast.makeText(context, "Pin shortcut is not supported by your launcher", Toast.LENGTH_SHORT).show()
        return
    }

    // Cleaned up intent structure targeting your activity
    val intent = Intent(context, AddNoteActivity::class.java).apply {
        action = Intent.ACTION_VIEW // Standard viewing action instead of old CREATE_SHORTCUT
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    // Construct the metadata wrapper
    val shortcutInfo = ShortcutInfo.Builder(context, "new_note_shortcut")
        .setShortLabel("Add Note")
        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
        .setIntent(intent)
        .build()

    // Android 14+ safe callback mechanism
    // Passing 'null' works, but the system won't tell you if the placement succeeded.
    shortcutManager.requestPinShortcut(shortcutInfo, null)

    // Optional: Warn the user that the system dialog handles actual placement
    Toast.makeText(context, "Follow the prompt to pin shortcut", Toast.LENGTH_SHORT).show()
}
