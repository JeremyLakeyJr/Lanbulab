package com.jeremylakeyjr.lanbulab.ui.printjobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremylakeyjr.lanbulab.data.model.JobStatus
import com.jeremylakeyjr.lanbulab.data.model.PrintJob
import com.jeremylakeyjr.lanbulab.databinding.FragmentPrintJobsBinding

class PrintJobsFragment : Fragment() {
    
    private var _binding: FragmentPrintJobsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var adapter: PrintJobsAdapter
    private val printJobs = mutableListOf<PrintJob>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrintJobsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        loadPrintJobs()
    }
    
    private fun setupRecyclerView() {
        adapter = PrintJobsAdapter(
            onJobClick = { job ->
                Toast.makeText(context, "Job: ${job.fileName}", Toast.LENGTH_SHORT).show()
            },
            onCancelClick = { job ->
                cancelJob(job)
            }
        )
        binding.jobsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.jobsRecyclerView.adapter = adapter
    }
    
    private fun loadPrintJobs() {
        // In production, load from database or API
        // For now, show sample data
        printJobs.clear()
        printJobs.addAll(listOf(
            PrintJob(
                id = "1",
                fileName = "sample_model.gcode",
                filePath = "/storage/sliced/sample_model.gcode",
                printerId = "printer_1",
                status = JobStatus.COMPLETED
            ),
            PrintJob(
                id = "2",
                fileName = "test_print.gcode",
                filePath = "/storage/sliced/test_print.gcode",
                printerId = "printer_1",
                status = JobStatus.PRINTING
            )
        ))
        
        adapter.submitList(printJobs)
        
        binding.emptyView.visibility = if (printJobs.isEmpty()) View.VISIBLE else View.GONE
    }
    
    private fun cancelJob(job: PrintJob) {
        Toast.makeText(context, "Cancelling job: ${job.fileName}", Toast.LENGTH_SHORT).show()
        // In production, send cancel command to printer
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
