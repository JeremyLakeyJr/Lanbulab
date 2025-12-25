package com.jeremylakeyjr.lanbulab.ui.printers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremylakeyjr.lanbulab.databinding.FragmentPrintersBinding
import com.jeremylakeyjr.lanbulab.service.PrinterDiscoveryService
import kotlinx.coroutines.launch

class PrintersFragment : Fragment() {
    
    private var _binding: FragmentPrintersBinding? = null
    private val binding get() = _binding!!
    
    private val discoveryService = PrinterDiscoveryService()
    private lateinit var adapter: PrintersAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrintersBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        discoverPrinters()
    }
    
    private fun setupRecyclerView() {
        adapter = PrintersAdapter { printer ->
            // Handle printer click
            Toast.makeText(context, "Selected: ${printer.name}", Toast.LENGTH_SHORT).show()
        }
        binding.printersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.printersRecyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.scanButton.setOnClickListener {
            discoverPrinters()
        }
        
        binding.addPrinterButton.setOnClickListener {
            // Show dialog to manually add printer
            Toast.makeText(context, "Manual add printer", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun discoverPrinters() {
        binding.progressBar.visibility = View.VISIBLE
        binding.scanButton.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val printers = discoveryService.discoverPrinters()
                adapter.submitList(printers)
                
                if (printers.isEmpty()) {
                    Toast.makeText(context, "No printers found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error discovering printers: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.scanButton.isEnabled = true
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
