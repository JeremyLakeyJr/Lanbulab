package com.jeremylakeyjr.lanbulab.ui.printers

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.jeremylakeyjr.lanbulab.data.model.BambuPrinter
import com.jeremylakeyjr.lanbulab.databinding.DialogAddPrinterBinding
import java.util.UUID

class AddPrinterDialogFragment : DialogFragment() {
    
    private var _binding: DialogAddPrinterBinding? = null
    private val binding get() = _binding!!
    
    private var onPrinterAdded: ((BambuPrinter) -> Unit)? = null
    
    fun setOnPrinterAddedListener(listener: (BambuPrinter) -> Unit) {
        onPrinterAdded = listener
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddPrinterBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
    }
    
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    
    private fun setupListeners() {
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        
        binding.addButton.setOnClickListener {
            addPrinter()
        }
    }
    
    private fun addPrinter() {
        val name = binding.printerNameInput.text.toString().trim()
        val ipAddress = binding.ipAddressInput.text.toString().trim()
        val accessCode = binding.accessCodeInput.text.toString().trim()
        val model = binding.printerModelInput.text.toString().trim()
        
        // Validate inputs
        if (name.isEmpty()) {
            binding.printerNameInput.error = "Name is required"
            return
        }
        
        if (ipAddress.isEmpty()) {
            binding.ipAddressInput.error = "IP address is required"
            return
        }
        
        // Basic IP validation
        if (!isValidIpAddress(ipAddress)) {
            binding.ipAddressInput.error = "Invalid IP address format"
            return
        }
        
        val printer = BambuPrinter(
            id = UUID.randomUUID().toString(),
            name = name,
            ipAddress = ipAddress,
            accessCode = accessCode,
            model = model.ifEmpty { "Unknown Model" },
            isOnline = false
        )
        
        onPrinterAdded?.invoke(printer)
        dismiss()
    }
    
    private fun isValidIpAddress(ip: String): Boolean {
        val parts = ip.split(".")
        if (parts.size != 4) return false
        
        return parts.all { part ->
            val num = part.toIntOrNull()
            num != null && num in 0..255
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        fun newInstance(): AddPrinterDialogFragment {
            return AddPrinterDialogFragment()
        }
    }
}
