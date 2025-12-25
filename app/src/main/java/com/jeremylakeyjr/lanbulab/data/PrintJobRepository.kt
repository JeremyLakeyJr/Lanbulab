package com.jeremylakeyjr.lanbulab.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeremylakeyjr.lanbulab.data.model.JobStatus
import com.jeremylakeyjr.lanbulab.data.model.PrintJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class PrintJobRepository(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _printJobs = MutableStateFlow<List<PrintJob>>(emptyList())
    val printJobs: StateFlow<List<PrintJob>> = _printJobs
    
    init {
        loadPrintJobs()
    }
    
    private fun loadPrintJobs() {
        val jobsJson = prefs.getString(KEY_PRINT_JOBS, null)
        if (jobsJson != null) {
            val type = object : TypeToken<List<PrintJob>>() {}.type
            val jobList = gson.fromJson<List<PrintJob>>(jobsJson, type)
            _printJobs.value = jobList
        }
    }
    
    suspend fun addPrintJob(job: PrintJob) = withContext(Dispatchers.IO) {
        val currentJobs = _printJobs.value.toMutableList()
        currentJobs.add(0, job) // Add to beginning of list
        savePrintJobs(currentJobs)
    }
    
    suspend fun updateJobStatus(jobId: String, status: JobStatus) = withContext(Dispatchers.IO) {
        val currentJobs = _printJobs.value.toMutableList()
        val index = currentJobs.indexOfFirst { it.id == jobId }
        if (index >= 0) {
            currentJobs[index] = currentJobs[index].copy(status = status)
            savePrintJobs(currentJobs)
        }
    }
    
    suspend fun removeJob(jobId: String) = withContext(Dispatchers.IO) {
        val currentJobs = _printJobs.value.toMutableList()
        currentJobs.removeAll { it.id == jobId }
        savePrintJobs(currentJobs)
    }
    
    suspend fun getActiveJobs(): List<PrintJob> = withContext(Dispatchers.IO) {
        _printJobs.value.filter { 
            it.status in listOf(JobStatus.PENDING, JobStatus.UPLOADING, JobStatus.UPLOADED, JobStatus.PRINTING)
        }
    }
    
    private fun savePrintJobs(jobs: List<PrintJob>) {
        val jobsJson = gson.toJson(jobs)
        prefs.edit().putString(KEY_PRINT_JOBS, jobsJson).apply()
        _printJobs.value = jobs
    }
    
    companion object {
        private const val PREFS_NAME = "lanbulab_prefs"
        private const val KEY_PRINT_JOBS = "print_jobs"
    }
}
