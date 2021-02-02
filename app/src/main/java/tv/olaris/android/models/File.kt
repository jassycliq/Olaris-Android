package tv.olaris.android.models

import fragment.FileBase

data class File(val fileName: String,
                val filePath: String,
                val uuid: String,
                val totalDuration: Double?,
                val fileSize: String,
                val fileBase: FileBase)
