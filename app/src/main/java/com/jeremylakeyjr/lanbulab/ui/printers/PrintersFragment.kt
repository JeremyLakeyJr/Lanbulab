package com.jeremylakeyjr.lanbulab.ui.printers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremylakeyjr.lanbulab.data.PrinterRepository
import com.jeremylakeyjr.lanbulab.databinding.FragmentPrintersBinding
import com.jeremylakeyjr.lanbulab.service.PrinterDiscoveryService
import kotlinx.coroutines.launch

class PrintersFragment : Fragment() {
    
    private var _binding: FragmentPrintersBinding? = null
    private val binding get() = _binding!!
    
    private val discoveryService = PrinterDiscoveryService()
    private lateinit var adapter: PrintersAdapter
    private lateinit var printerRepository: PrinterRepository
    
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
        
        printerRepository = PrinterRepository(requireContext())
        
        setupRecyclerView()
        setupListeners()
        observePrinters()
        loadInitialPrinters()
    }
    
    private fun setupRecyclerView() {
        adapter = PrintersAdapter(
            onPrinterClick = { printer ->
                // Handle printer click
                Toast.makeText(context, "Selected: ${printer.name}", Toast.LENGTH_SHORT).show()
            },
            onPrinterLongClick = { printer ->
                // Handle long click - show options to edit or delete
                showPrinterOptions(printer)
                true
            }
        )
        binding.printersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.printersRecyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.scanButton.setOnClickListener {
            discoverPrinters()
        }
        
        binding.addPrinterButton.setOnClickListener {
            showAddPrinterDialog()
        }
    }
    
    private fun observePrinters() {
        lifecycleScope.launch {
            printerRepository.printers.collect { printers ->
                adapter.submitList(printers)
                updateEmptyState(printers.isEmpty())
            }
        }
    }
    
    private fun loadInitialPrinters() {
        // If no printers, show empty state
        if (printerRepository.printers.value.isEmpty()) {
            updateEmptyState(true)
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.printersRecyclerView.visibility = View.GONE
            // Create a simple empty state message
            Toast.makeText(context, "No printers yet. Click 'Add' or 'Scan' to add printers.", Toast.LENGTH_LONG).show()
        } else {
            binding.printersRecyclerView.visibility = View.VISIBLE
        }
    }
    
    private fun showAddPrinterDialog() {
        val dialog = AddPrinterDialogFragment.newInstance()
        dialog.setOnPrinterAddedListener { printer ->
            lifecycleScope.launch {
                printerRepository.addPrinter(printer)
                Toast.makeText(context, "Printer added: ${printer.name}", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show(childFragmentManager, "add_printer")
    }
    
    private fun showPrinterOptions(printer: com.jeremylakeyjr.lanbulab.data.model.BambuPrinter) {
        val options = arrayOf("Delete")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle(printer.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> deletePrinter(printer)
                }
            }
            .show()
    }
    
    private fun deletePrinter(printer: com.jeremylakeyjr.lanbulab.data.model.BambuPrinter) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Printer")
            .setMessage("Are you sure you want to delete ${printer.name}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    printerRepository.removePrinter(printer.id)
                    Toast.makeText(context, "Printer deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun discoverPrinters() {
        binding.progressBar.visibility = View.VISIBLE
        binding.scanButton.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val printers = discoveryService.discoverPrinters()
                
                if (printers.isEmpty()) {
                    Toast.makeText(context, "No printers found on network", Toast.LENGTH_SHORT).show()
                } else {
                    printerRepository.addDiscoveredPrinters(printers)
                    Toast.makeText(context, "Found ${printers.size} printer(s)", Toast.LENGTH_SHORT).show()
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
