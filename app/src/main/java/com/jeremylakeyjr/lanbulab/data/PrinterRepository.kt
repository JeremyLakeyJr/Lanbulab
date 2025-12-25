package com.jeremylakeyjr.lanbulab.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeremylakeyjr.lanbulab.data.model.BambuPrinter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class PrinterRepository(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _printers = MutableStateFlow<List<BambuPrinter>>(emptyList())
    val printers: StateFlow<List<BambuPrinter>> = _printers
    
    init {
        loadPrinters()
    }
    
    private fun loadPrinters() {
        val printersJson = prefs.getString(KEY_PRINTERS, null)
        if (printersJson != null) {
            val type = object : TypeToken<List<BambuPrinter>>() {}.type
            val printerList = gson.fromJson<List<BambuPrinter>>(printersJson, type)
            _printers.value = printerList
        }
    }
    
    suspend fun addPrinter(printer: BambuPrinter) = withContext(Dispatchers.IO) {
        val currentPrinters = _printers.value.toMutableList()
        // Check if printer already exists
        val existingIndex = currentPrinters.indexOfFirst { it.id == printer.id }
        if (existingIndex >= 0) {
            currentPrinters[existingIndex] = printer
        } else {
            currentPrinters.add(printer)
        }
        savePrinters(currentPrinters)
    }
    
    suspend fun removePrinter(printerId: String) = withContext(Dispatchers.IO) {
        val currentPrinters = _printers.value.toMutableList()
        currentPrinters.removeAll { it.id == printerId }
        savePrinters(currentPrinters)
    }
    
    suspend fun updatePrinter(printer: BambuPrinter) = withContext(Dispatchers.IO) {
        val currentPrinters = _printers.value.toMutableList()
        val index = currentPrinters.indexOfFirst { it.id == printer.id }
        if (index >= 0) {
            currentPrinters[index] = printer
            savePrinters(currentPrinters)
        }
    }
    
    suspend fun getPrinter(printerId: String): BambuPrinter? = withContext(Dispatchers.IO) {
        _printers.value.find { it.id == printerId }
    }
    
    suspend fun addDiscoveredPrinters(discoveredPrinters: List<BambuPrinter>) = withContext(Dispatchers.IO) {
        val currentPrinters = _printers.value.toMutableList()
        discoveredPrinters.forEach { discovered ->
            // Only add if not already in list
            if (currentPrinters.none { it.ipAddress == discovered.ipAddress }) {
                currentPrinters.add(discovered)
            } else {
                // Update online status for existing printer
                val index = currentPrinters.indexOfFirst { it.ipAddress == discovered.ipAddress }
                if (index >= 0) {
                    currentPrinters[index] = currentPrinters[index].copy(isOnline = true)
                }
            }
        }
        savePrinters(currentPrinters)
    }
    
    private fun savePrinters(printers: List<BambuPrinter>) {
        val printersJson = gson.toJson(printers)
        prefs.edit().putString(KEY_PRINTERS, printersJson).apply()
        _printers.value = printers
    }
    
    companion object {
        private const val PREFS_NAME = "lanbulab_prefs"
        private const val KEY_PRINTERS = "printers"
    }
}
