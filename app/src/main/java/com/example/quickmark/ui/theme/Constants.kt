package com.example.quickmark.ui.theme

object Constants {
    const val FILE_NAME = "FILE_NAME"
    const val ALLOW_STORAGE_PERMISSION_MSG = "Please allow storage permission for app functionality. If you've granted permission, please restart the app for changes to take effect."
    const val SELECT_DIRECTORY_PATH_MSG = "Please go to settings and select the directory path where you want to store your notes."
    object ExceptionToast{
        const val FILE_ALREADY_EXIST = "File with the same name already exists. Please choose a different name."
        const val GENERAL = "Something is wrong!"
        const val VALID_TITLE = "Please Enter Valid Title!"
        const val NO_CHANGES = "No changes made to the file."

    }

    object Labels {
        const val BACK = "Navigate to previous screen"
        object HomeScreen{
            const val MENU = "Open Application Drawer"
            const val DELETE = "Delete Selected Notes"
            const val CLEAR = "Clear Selection"
            const val SETTINGS = "Open Settings"
            const val ADD = "Add New Note"
        }

        object SettingScreen{
            const val EDIT_DIRECTORY_PATH = "Edit Directory Path"
            const val SAVE_DIRECTORY_PATH = "Save Directory Path"
        }

        object AddEdit{
            const val CLEAR = "Clear Note Title"
            const val SAVE = "Save"
        }

    }
}