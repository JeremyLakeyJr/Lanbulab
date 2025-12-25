package com.jeremylakeyjr.lanbulab.ui.makerworld

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jeremylakeyjr.lanbulab.R
import com.jeremylakeyjr.lanbulab.data.PrintJobRepository
import com.jeremylakeyjr.lanbulab.data.model.JobStatus
import com.jeremylakeyjr.lanbulab.data.model.MakerWorldModel
import com.jeremylakeyjr.lanbulab.data.model.PrintJob
import com.jeremylakeyjr.lanbulab.data.model.SliceSettings
import com.jeremylakeyjr.lanbulab.databinding.DialogSliceSettingsBinding
import com.jeremylakeyjr.lanbulab.service.MakerWorldService
import com.jeremylakeyjr.lanbulab.service.SlicingService
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class SliceDialogFragment : DialogFragment() {
    
    private var _binding: DialogSliceSettingsBinding? = null
    private val binding get() = _binding!!
    
    private val makerWorldService = MakerWorldService()
    private val slicingService = SlicingService()
    private lateinit var printJobRepository: PrintJobRepository
    
    private lateinit var model: MakerWorldModel
    
    companion object {
        private const val ARG_MODEL_ID = "model_id"
        private const val ARG_MODEL_NAME = "model_name"
        private const val ARG_MODEL_DOWNLOAD_URL = "model_download_url"
        
        fun newInstance(model: MakerWorldModel): SliceDialogFragment {
            val fragment = SliceDialogFragment()
            val args = Bundle().apply {
                putString(ARG_MODEL_ID, model.id)
                putString(ARG_MODEL_NAME, model.name)
                putString(ARG_MODEL_DOWNLOAD_URL, model.downloadUrl)
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
        _binding = DialogSliceSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        printJobRepository = PrintJobRepository(requireContext())
        
        // Reconstruct model from arguments
        model = MakerWorldModel(
            id = arguments?.getString(ARG_MODEL_ID) ?: "",
            name = arguments?.getString(ARG_MODEL_NAME) ?: "",
            author = "",
            thumbnailUrl = "",
            downloadUrl = arguments?.getString(ARG_MODEL_DOWNLOAD_URL) ?: ""
        )
        
        setupViews()
        setupListeners()
    }
    
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    
    private fun setupViews() {
        binding.modelNameText.text = model.name
        
        // Setup filament type spinner
        val filamentTypes = arrayOf("PLA", "ABS", "PETG", "TPU")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filamentTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filamentTypeSpinner.adapter = adapter
        
        // Set default values
        binding.layerHeightInput.setText("0.2")
        binding.infillInput.setText("15")
        binding.printSpeedInput.setText("100")
        binding.bedTempInput.setText("60")
        binding.nozzleTempInput.setText("220")
    }
    
    private fun setupListeners() {
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        
        binding.sliceButton.setOnClickListener {
            sliceModel()
        }
        
        // Update temperatures when filament type changes
        binding.filamentTypeSpinner.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val filamentType = parent?.getItemAtPosition(position).toString()
                    val defaults = slicingService.getDefaultSettings(filamentType)
                    binding.bedTempInput.setText(defaults.bedTemperature.toString())
                    binding.nozzleTempInput.setText(defaults.nozzleTemperature.toString())
                }
                
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        )
    }
    
    private fun sliceModel() {
        binding.progressBar.visibility = View.VISIBLE
        binding.sliceButton.isEnabled = false
        
        lifecycleScope.launch {
            try {
                // First download the model if not already downloaded
                val modelFile = if (model.isDownloaded && model.localPath.isNotEmpty()) {
                    File(model.localPath)
                } else {
                    val downloadDir = requireContext().getExternalFilesDir("models")
                        ?: requireContext().filesDir
                    Toast.makeText(context, "Downloading model...", Toast.LENGTH_SHORT).show()
                    
                    // Create a dummy model file for demonstration
                    val dummyFile = File(downloadDir, "${model.id}_${model.name}.3mf")
                    if (!downloadDir.exists()) {
                        downloadDir.mkdirs()
                    }
                    dummyFile.writeText("# Dummy 3MF file for ${model.name}")
                    dummyFile
                }
                
                if (modelFile == null || !modelFile.exists()) {
                    Toast.makeText(context, "Failed to download model", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                // Get slice settings from UI
                val settings = SliceSettings(
                    layerHeight = binding.layerHeightInput.text.toString().toFloatOrNull() ?: 0.2f,
                    infillDensity = binding.infillInput.text.toString().toIntOrNull() ?: 15,
                    printSpeed = binding.printSpeedInput.text.toString().toIntOrNull() ?: 100,
                    supportEnabled = binding.supportCheckbox.isChecked,
                    bedTemperature = binding.bedTempInput.text.toString().toIntOrNull() ?: 60,
                    nozzleTemperature = binding.nozzleTempInput.text.toString().toIntOrNull() ?: 220,
                    filamentType = binding.filamentTypeSpinner.selectedItem.toString()
                )
                
                // Slice the model
                Toast.makeText(context, "Slicing model...", Toast.LENGTH_SHORT).show()
                val outputDir = requireContext().getExternalFilesDir("sliced")
                    ?: requireContext().filesDir
                val slicedFile = slicingService.sliceModel(modelFile, settings, outputDir)
                
                if (slicedFile != null && slicedFile.exists()) {
                    // Create a print job
                    val printJob = PrintJob(
                        id = UUID.randomUUID().toString(),
                        fileName = slicedFile.name,
                        filePath = slicedFile.absolutePath,
                        printerId = "", // Will be assigned when sending to printer
                        status = JobStatus.PENDING
                    )
                    
                    printJobRepository.addPrintJob(printJob)
                    
                    Toast.makeText(context, "Model sliced successfully! Check Print Jobs tab.", Toast.LENGTH_LONG).show()
                    dismiss()
                } else {
                    Toast.makeText(context, "Failed to slice model", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.sliceButton.isEnabled = true
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
