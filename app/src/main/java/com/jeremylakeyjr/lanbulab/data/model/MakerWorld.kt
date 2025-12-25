package com.jeremylakeyjr.lanbulab.data.model

data class MakerWorldModel(
    val id: String,
    val name: String,
    val author: String,
    val thumbnailUrl: String,
    val downloadUrl: String,
    val description: String = "",
    val tags: List<String> = emptyList(),
    val likes: Int = 0,
    val downloads: Int = 0,
    val fileSize: Long = 0,
    val fileFormat: String = "3mf",
    val isDownloaded: Boolean = false,
    val localPath: String = ""
)

data class PrintJob(
    val id: String,
    val fileName: String,
    val filePath: String,
    val printerId: String,
    val status: JobStatus = JobStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class JobStatus {
    PENDING,
    UPLOADING,
    UPLOADED,
    PRINTING,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class SliceSettings(
    val layerHeight: Float = 0.2f,
    val infillDensity: Int = 15,
    val printSpeed: Int = 100,
    val supportEnabled: Boolean = false,
    val bedTemperature: Int = 60,
    val nozzleTemperature: Int = 220,
    val filamentType: String = "PLA"
)
