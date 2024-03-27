package com.example.quickmark

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.flashup.ui.theme.QuickMarkTheme
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.navigation.HomeScreenNavGraph
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_QuickMark)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val context = LocalContext.current
            if (checkPermission(context))
                QuickMarkTheme {
                    HomeScreenNavGraph(navController = rememberNavController())
                }
            else
                requestPermission(context)
        }
    }

    private fun checkPermission(context: Context):Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readFile=ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
            val writFile=ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
            readFile && writFile
        }

    }
    private fun requestPermission(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent =Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data=Uri.parse(String.format("package:%s",applicationContext.packageName))
            context.startActivity(intent)
        } else {
            val requestReadWritePermissionLaucher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                    result ->
                var count:Int=0
                result.entries.forEach {
                    if (it.value==true) {
                        count++
                    }
                }
                if (count==2) {
                    Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Permission not Granted",Toast.LENGTH_SHORT).show()
                }

            }
            requestReadWritePermissionLaucher.launch(arrayOf(READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE))
        }
    }




}





