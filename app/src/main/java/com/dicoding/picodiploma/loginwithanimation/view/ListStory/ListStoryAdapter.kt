package com.dicoding.picodiploma.loginwithanimation.view.ListStory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.main.ListAllStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding

class ListStoryAdapter(private val onItemClick: (ListAllStoryItem) -> Unit) : ListAdapter<ListAllStoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK)  {

    companion object {
        // DiffUtil untuk membandingkan item dan konten
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListAllStoryItem>() {
            override fun areItemsTheSame(oldItem: ListAllStoryItem, newItem: ListAllStoryItem): Boolean {
                // Perbandingan apakah item sama berdasarkan ID
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListAllStoryItem, newItem: ListAllStoryItem): Boolean {
                // Perbandingan apakah konten item sama
                return oldItem == newItem
            }
        }
    }

    // ViewHolder untuk menahan referensi tampilan item
    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListAllStoryItem, onItemClick: (ListAllStoryItem) -> Unit) {
            // Binding data ke UI
            binding.tvItemName.text = item.name
            binding.tvItemDesc.text = item.description
            // Menggunakan Glide untuk memuat gambar
            Glide.with(binding.root.context).load(item.photoUrl).into(binding.ivItemPhoto)

            // Tambahkan tindakan klik untuk membuka detail event
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    // Meng-inflate item layout dan membuat ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // Mengikat data ke ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

}