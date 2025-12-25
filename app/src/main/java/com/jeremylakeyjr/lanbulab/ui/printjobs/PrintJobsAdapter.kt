package com.jeremylakeyjr.lanbulab.ui.printjobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeremylakeyjr.lanbulab.data.model.JobStatus
import com.jeremylakeyjr.lanbulab.data.model.PrintJob
import com.jeremylakeyjr.lanbulab.databinding.ItemPrintJobBinding

class PrintJobsAdapter(
    private val onJobClick: (PrintJob) -> Unit,
    private val onCancelClick: (PrintJob) -> Unit
) : ListAdapter<PrintJob, PrintJobsAdapter.JobViewHolder>(JobDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemPrintJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class JobViewHolder(
        private val binding: ItemPrintJobBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(job: PrintJob) {
            binding.jobFileName.text = job.fileName
            binding.jobStatus.text = job.status.name
            
            // Show cancel button only for certain statuses
            binding.cancelButton.visibility = when (job.status) {
                JobStatus.PENDING, JobStatus.UPLOADING, JobStatus.PRINTING -> android.view.View.VISIBLE
                else -> android.view.View.GONE
            }
            
            binding.root.setOnClickListener {
                onJobClick(job)
            }
            
            binding.cancelButton.setOnClickListener {
                onCancelClick(job)
            }
        }
    }
    
    private class JobDiffCallback : DiffUtil.ItemCallback<PrintJob>() {
        override fun areItemsTheSame(oldItem: PrintJob, newItem: PrintJob): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: PrintJob, newItem: PrintJob): Boolean {
            return oldItem == newItem
        }
    }
}
