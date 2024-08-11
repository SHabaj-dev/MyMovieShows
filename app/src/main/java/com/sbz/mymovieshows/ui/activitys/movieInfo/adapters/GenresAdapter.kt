package com.sbz.mymovieshows.ui.activitys.movieInfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sbz.mymovieshows.databinding.RvItemGenresBinding
import com.sbz.mymovieshows.models.Genre

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresAdapter.ViewHolder {
        val binding =
            RvItemGenresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresAdapter.ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.binding.tvGenreName.text = currentItem.name

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: RvItemGenresBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}