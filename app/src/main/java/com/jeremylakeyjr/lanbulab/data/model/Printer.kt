package com.jeremylakeyjr.lanbulab.data.model

data class BambuPrinter(
    val id: String,
    val name: String,
    val ipAddress: String,
    val port: Int = 8883,
    val accessCode: String = "",
    val serialNumber: String = "",
    val model: String = "",
    val isOnline: Boolean = false
)

data class PrinterStatus(
    val printerId: String,
    val state: PrintState,
    val temperature: Temperature,
    val progress: Int = 0,
    val currentFile: String = "",
    val remainingTime: Long = 0
)

enum class PrintState {
    IDLE,
    PRINTING,
    PAUSED,
    FINISHED,
    FAILED,
    OFFLINE
}

data class Temperature(
    val nozzle: Float = 0f,
    val bed: Float = 0f,
    val chamber: Float = 0f
)
