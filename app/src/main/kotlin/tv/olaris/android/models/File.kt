package tv.olaris.android.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import tv.olaris.android.fragment.FileBase

@Serializable
data class File(
    val fileName: String,
    val filePath: String,
    val uuid: String,
    val totalDuration: Double?,
    val fileSize: String,
    @Contextual
    val fileBase: FileBase,
)
