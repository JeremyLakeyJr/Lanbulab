package com.jeremylakeyjr.lanbulab.ui.makerworld

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeremylakeyjr.lanbulab.data.model.MakerWorldModel
import com.jeremylakeyjr.lanbulab.databinding.ItemMakerworldModelBinding

class MakerWorldAdapter(
    private val onModelClick: (MakerWorldModel) -> Unit,
    private val onSliceClick: (MakerWorldModel) -> Unit
) : ListAdapter<MakerWorldModel, MakerWorldAdapter.ModelViewHolder>(ModelDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val binding = ItemMakerworldModelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModelViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ModelViewHolder(
        private val binding: ItemMakerworldModelBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(model: MakerWorldModel) {
            binding.modelName.text = model.name
            binding.modelAuthor.text = "by ${model.author}"
            binding.modelLikes.text = "${model.likes} ♥"
            binding.modelDownloads.text = "${model.downloads} ↓"
            
            // Handle model click to view details
            binding.root.setOnClickListener {
                onModelClick(model)
            }
            
            // Handle slice button click - NEW REQUIREMENT
            binding.sliceButton.setOnClickListener {
                onSliceClick(model)
            }
        }
    }
    
    private class ModelDiffCallback : DiffUtil.ItemCallback<MakerWorldModel>() {
        override fun areItemsTheSame(oldItem: MakerWorldModel, newItem: MakerWorldModel): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: MakerWorldModel, newItem: MakerWorldModel): Boolean {
            return oldItem == newItem
        }
    }
}
