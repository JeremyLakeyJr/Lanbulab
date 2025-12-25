package com.jeremylakeyjr.lanbulab.ui.printers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeremylakeyjr.lanbulab.data.model.BambuPrinter
import com.jeremylakeyjr.lanbulab.databinding.ItemPrinterBinding

class PrintersAdapter(
    private val onPrinterClick: (BambuPrinter) -> Unit,
    private val onPrinterLongClick: (BambuPrinter) -> Boolean = { false }
) : ListAdapter<BambuPrinter, PrintersAdapter.PrinterViewHolder>(PrinterDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrinterViewHolder {
        val binding = ItemPrinterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PrinterViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PrinterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class PrinterViewHolder(
        private val binding: ItemPrinterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(printer: BambuPrinter) {
            binding.printerName.text = printer.name
            binding.printerModel.text = printer.model
            binding.printerIp.text = printer.ipAddress
            binding.statusIndicator.isSelected = printer.isOnline
            
            binding.root.setOnClickListener {
                onPrinterClick(printer)
            }
            
            binding.root.setOnLongClickListener {
                onPrinterLongClick(printer)
            }
        }
    }
    
    private class PrinterDiffCallback : DiffUtil.ItemCallback<BambuPrinter>() {
        override fun areItemsTheSame(oldItem: BambuPrinter, newItem: BambuPrinter): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: BambuPrinter, newItem: BambuPrinter): Boolean {
            return oldItem == newItem
        }
    }
}
