package com.jeremylakeyjr.lanbulab.ui.makerworld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.jeremylakeyjr.lanbulab.databinding.FragmentMakerworldBinding
import com.jeremylakeyjr.lanbulab.service.MakerWorldService
import kotlinx.coroutines.launch

class MakerWorldFragment : Fragment() {
    
    private var _binding: FragmentMakerworldBinding? = null
    private val binding get() = _binding!!
    
    private val makerWorldService = MakerWorldService()
    private lateinit var adapter: MakerWorldAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakerworldBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        loadModels()
    }
    
    private fun setupRecyclerView() {
        adapter = MakerWorldAdapter(
            onModelClick = { model ->
                // Show model details
                Toast.makeText(context, "Model: ${model.name}", Toast.LENGTH_SHORT).show()
            },
            onSliceClick = { model ->
                // Handle slice button click - this is the new requirement
                showSliceDialog(model)
            }
        )
        binding.modelsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.modelsRecyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            searchModels(query)
        }
        
        binding.refreshButton.setOnClickListener {
            loadModels()
        }
    }
    
    private fun loadModels() {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val models = makerWorldService.getPopularModels()
                adapter.submitList(models)
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading models: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun searchModels(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val models = makerWorldService.searchModels(query)
                adapter.submitList(models)
            } catch (e: Exception) {
                Toast.makeText(context, "Error searching: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun showSliceDialog(model: com.jeremylakeyjr.lanbulab.data.model.MakerWorldModel) {
        // Show slice settings dialog
        val dialog = SliceDialogFragment.newInstance(model)
        dialog.show(childFragmentManager, "slice_dialog")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
