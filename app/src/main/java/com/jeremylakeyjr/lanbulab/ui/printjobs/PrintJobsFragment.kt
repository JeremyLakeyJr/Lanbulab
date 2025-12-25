package com.jeremylakeyjr.lanbulab.ui.printjobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremylakeyjr.lanbulab.data.PrintJobRepository
import com.jeremylakeyjr.lanbulab.data.model.JobStatus
import com.jeremylakeyjr.lanbulab.databinding.FragmentPrintJobsBinding
import kotlinx.coroutines.launch

class PrintJobsFragment : Fragment() {
    
    private var _binding: FragmentPrintJobsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var adapter: PrintJobsAdapter
    private lateinit var printJobRepository: PrintJobRepository
    
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
        
        printJobRepository = PrintJobRepository(requireContext())
        
        setupRecyclerView()
        observePrintJobs()
    }
    
    private fun setupRecyclerView() {
        adapter = PrintJobsAdapter(
            onJobClick = { job ->
                // Show dialog to send to printer if pending
                if (job.status == JobStatus.PENDING) {
                    showSelectPrinterDialog(job)
                } else {
                    Toast.makeText(context, "Job: ${job.fileName} - ${job.status.name}", Toast.LENGTH_SHORT).show()
                }
            },
            onCancelClick = { job ->
                cancelJob(job)
            },
            onDeleteClick = { job ->
                deleteJob(job)
            }
        )
        binding.jobsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.jobsRecyclerView.adapter = adapter
    }
    
    private fun showSelectPrinterDialog(job: com.jeremylakeyjr.lanbulab.data.model.PrintJob) {
        val dialog = SelectPrinterDialogFragment.newInstance(job)
        dialog.show(childFragmentManager, "select_printer")
    }
    
    private fun observePrintJobs() {
        lifecycleScope.launch {
            printJobRepository.printJobs.collect { jobs ->
                adapter.submitList(jobs)
                binding.emptyView.visibility = if (jobs.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    
    private fun cancelJob(job: com.jeremylakeyjr.lanbulab.data.model.PrintJob) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Cancel Print Job")
            .setMessage("Are you sure you want to cancel ${job.fileName}?")
            .setPositiveButton("Cancel Job") { _, _ ->
                lifecycleScope.launch {
                    printJobRepository.updateJobStatus(job.id, JobStatus.CANCELLED)
                    Toast.makeText(context, "Job cancelled", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun deleteJob(job: com.jeremylakeyjr.lanbulab.data.model.PrintJob) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Print Job")
            .setMessage("Remove ${job.fileName} from history?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    printJobRepository.removeJob(job.id)
                    Toast.makeText(context, "Job deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
