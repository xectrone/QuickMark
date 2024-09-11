package com.xectrone.quickmark.ui.theme

object Constants {
    const val FILE_URI = "FILE_URI"
    const val SELECT_DIRECTORY_PATH_MSG = "Please go to Settings and select the note folder where you want to store your notes."
    object ExceptionToast{
        const val FILE_ALREADY_EXIST = "File with the same name already exists. Please choose a different name."
        const val GENERAL = "Something is wrong!"
        const val VALID_TITLE = "Please Enter Valid Title!"
        const val NO_CHANGES = "No changes made to the file."
        const val NO_VALID_FILE_NAME = "Invalid Title: Only letters, numbers, hyphens, underscores, spaces, and periods are allowed."

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

        object SortOptions {
            const val nameASC = "By Name ASC"
            const val nameDESC = "By Name DESC"
            const val lastModifiedASC = "By Date ASC"
            const val lastModifiedDESC = "By Date DESC"
        }
    }


}