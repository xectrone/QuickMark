package com.example.quickmark.domain.file_handling

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import java.nio.file.WatchService

class FileObserver(private val directoryPath: String) {
    private var watchService: WatchService? = null
    private var watchKey: WatchKey? = null

    init {
        val path: Path = FileSystems.getDefault().getPath(directoryPath)
        watchService = path.fileSystem.newWatchService()
        watchKey = path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
    }

    fun startWatching(callback: () -> Unit) {
        while (true) {
            val key = watchService?.take() ?: break
            for (event in key.pollEvents()) {
                callback.invoke()
            }
            key.reset()
        }
    }

    fun stopWatching() {
        watchKey?.cancel()
        watchService?.close()
    }
}
