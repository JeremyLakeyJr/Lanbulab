package com.jeremylakeyjr.lanbulab.ui.printjobs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremylakeyjr.lanbulab.data.PrintJobRepository
import com.jeremylakeyjr.lanbulab.data.PrinterRepository
import com.jeremylakeyjr.lanbulab.data.model.BambuPrinter
import com.jeremylakeyjr.lanbulab.data.model.JobStatus
import com.jeremylakeyjr.lanbulab.data.model.PrintJob
import com.jeremylakeyjr.lanbulab.databinding.DialogSelectPrinterBinding
import com.jeremylakeyjr.lanbulab.service.PrinterService
import com.jeremylakeyjr.lanbulab.ui.printers.PrintersAdapter
import kotlinx.coroutines.launch
import java.io.File

class SelectPrinterDialogFragment : DialogFragment() {
    
    private var _binding: DialogSelectPrinterBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var printerRepository: PrinterRepository
    private lateinit var printJobRepository: PrintJobRepository
    private val printerService = PrinterService()
    private lateinit var adapter: PrintersAdapter
    private lateinit var printJob: PrintJob
    
    companion object {
        private const val ARG_JOB_ID = "job_id"
        private const val ARG_JOB_FILE_NAME = "job_file_name"
        private const val ARG_JOB_FILE_PATH = "job_file_path"
        
        fun newInstance(job: PrintJob): SelectPrinterDialogFragment {
            val fragment = SelectPrinterDialogFragment()
            val args = Bundle().apply {
                putString(ARG_JOB_ID, job.id)
                putString(ARG_JOB_FILE_NAME, job.fileName)
                putString(ARG_JOB_FILE_PATH, job.filePath)
            }
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectPrinterBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        printerRepository = PrinterRepository(requireContext())
        printJobRepository = PrintJobRepository(requireContext())
        
        printJob = PrintJob(
            id = arguments?.getString(ARG_JOB_ID) ?: "",
            fileName = arguments?.getString(ARG_JOB_FILE_NAME) ?: "",
            filePath = arguments?.getString(ARG_JOB_FILE_PATH) ?: "",
            printerId = ""
        )
        
        setupViews()
        observePrinters()
    }
    
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    
    private fun setupViews() {
        binding.jobNameText.text = "Send: ${printJob.fileName}"
        
        adapter = PrintersAdapter(
            onPrinterClick = { printer ->
                sendToPrinter(printer)
            },
            onPrinterLongClick = { false }
        )
        binding.printersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.printersRecyclerView.adapter = adapter
        
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
    
    private fun observePrinters() {
        lifecycleScope.launch {
            printerRepository.printers.collect { printers ->
                adapter.submitList(printers)
                
                if (printers.isEmpty()) {
                    binding.emptyText.visibility = View.VISIBLE
                    binding.printersRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyText.visibility = View.GONE
                    binding.printersRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }
    
    private fun sendToPrinter(printer: BambuPrinter) {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val file = File(printJob.filePath)
                if (!file.exists()) {
                    Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                // Update job status to uploading
                printJobRepository.updateJobStatus(printJob.id, JobStatus.UPLOADING)
                Toast.makeText(context, "Sending to ${printer.name}...", Toast.LENGTH_SHORT).show()
                
                // In a real app, this would upload to the printer
                // For now, we'll simulate it
                val success = printerService.sendPrintJob(printer, printJob.copy(printerId = printer.id))
                
                if (success) {
                    printJobRepository.updateJobStatus(printJob.id, JobStatus.UPLOADED)
                    Toast.makeText(context, "Sent to ${printer.name}!", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    printJobRepository.updateJobStatus(printJob.id, JobStatus.FAILED)
                    Toast.makeText(context, "Failed to send to printer", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                printJobRepository.updateJobStatus(printJob.id, JobStatus.FAILED)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
