package com.dicoding.picodiploma.loginwithanimation.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.dicoding.picodiploma.loginwithanimation.data.response.main.ItemStory
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    private val list = ArrayList<ItemStory>()
    private var onItemClickCallback: OnItemClickcallBack? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickcallBack) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(stories: ArrayList<ItemStory>) {
        list.clear()
        list.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class DataViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ItemStory) {
            binding.tvItemName.text = story.name
            binding.tvItemDesc.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .centerCrop()
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(story)
            }
        }
    }

    interface OnItemClickcallBack {
        fun onItemClicked(data: ItemStory)
    }
}
