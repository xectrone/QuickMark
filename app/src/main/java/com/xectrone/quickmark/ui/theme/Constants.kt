package com.xectrone.quickmark.ui.theme

object Constants {
    const val FILE_URI = "FILE_URI"
    const val SELECT_DIRECTORY_PATH_MSG = "Please go to Settings and select the note folder where you want to store your notes."
    const val DONATION_MSG = "Hi, I’m xectrone, an independent developer passionate about creating productivity tools.\n" +
            "\n" +
            "I love building apps that save time, simplify workflows, and boost productivity. My vision is to continue developing apps full-time, especially in areas like note-taking, study helpers, and utility tools that make life easier for everyone.\n" +
            "\n" +
            "If you’ve enjoyed using QuickMark or would like to support my journey, your donation would mean the world to me. It helps me focus on creating more amazing features, maintaining the app, and developing new apps that bring value to users like you."

    object ExceptionToast{
        const val FILE_ALREADY_EXIST = "File with the same name already exists. Please choose a different name."
        const val GENERAL = "Something is wrong!"
        const val VALID_TITLE = "Please Enter Valid Title!"
        const val NO_CHANGES = "No changes made to the file."
        const val NO_VALID_FILE_NAME = "Invalid Title: Only letters, numbers, hyphens, underscores, spaces, and periods are allowed."


    }

    object Donation{
        const val DONATION = "donation"
        const val SUPPORT = "support"
        const val THANKYOU = "thankyou"
    }

    object Toast{
        const val DOUBLE_BACK = "Press back again to exit"
    }

    object Labels {
        const val BACK = "Navigate to previous screen"
        object HomeScreen{
            const val MENU = "Open Application Drawer"
            const val DELETE = "Delete Selected Notes"
            const val CLEAR = "Clear Selection"
            const val SETTINGS = "Open Settings"
            const val ADD = "Add New Note"
            const val SORT = "Show Sorting Options"
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