package com.example.remoteaccountingapplication.domain

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class Backup {

    fun createTodayBackupDir(todayDateY: String): File {
        val docFolder = File("${Environment.getExternalStorageDirectory()}/Documents")
        val backupFolder =
            File(
                "${
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                }/RemoteAccountingBackup"
            )

        val absolutPath: String = getAbsolutBackupPath(docFolder, backupFolder)
        countBackups(backupFolder)

        val filePath = String.format(
            "%1s/%2s",
            absolutPath,
            todayDateY
        )

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        return fileDir
    }

    private fun getAbsolutBackupPath(docFolder: File, backupFolder: File): String {
        if (!docFolder.exists()) {
            docFolder.mkdir()
            backupFolder.mkdir()

            return String.format(
                "%1s/%2s",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "RemoteAccountingBackup"
            )
        } else {
            if (!backupFolder.exists()) {
                backupFolder.mkdir()
            }

            return String.format(
                "%1s/%2s",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "RemoteAccountingBackup"
            )
        }
    }

    private fun countBackups(backupFolder: File) {
        if (backupFolder.exists() && backupFolder.isDirectory) {
            val subFolders =
                backupFolder.listFiles { file -> file.isDirectory }?.toList() ?: emptyList()

            if (subFolders.size > 5) {
                val sortedSubFolders = subFolders.sortedBy { it.lastModified() }
                val oldestFolder = sortedSubFolders.first()
                deleteRecursively(oldestFolder)
            }
        }
    }

    private fun deleteRecursively(file: File): Boolean {
        if (file.isDirectory) {
            val children = file.listFiles() ?: return false
            for (child in children) {
                if (!deleteRecursively(child)) {
                    return false
                }
            }
        }
        return file.delete()
    }

    fun createTodayBackupFile(fileDir: File, csvTitle: String): File {
        return File(fileDir, csvTitle)
    }

    fun writeBackup(table: List<String>, csvFile: File) {
        try {
            val stream = FileOutputStream(csvFile)
            val writer = OutputStreamWriter(stream, "UTF-16")

            for (line in table) {
                writer.write(line)
                writer.write("\n")
            }

            writer.flush()
            writer.close()
            stream.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}