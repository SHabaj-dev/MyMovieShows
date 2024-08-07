package com.sbz.mymovieshows.ui.fragments.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sbz.mymovieshows.databinding.RvItemMoviesBinding
import com.sbz.mymovieshows.models.Result
import com.sbz.mymovieshows.utils.Constants.IMAGE_BASE_URL

class TopRatedMoviesAdapter :
    RecyclerView.Adapter<TopRatedMoviesAdapter.TopRatedMoviesViewHolder>() {



    inner class TopRatedMoviesViewHolder(val binding: RvItemMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val diffUtil = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMoviesViewHolder {
        val binding =
            RvItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopRatedMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRatedMoviesViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        val posterLink = IMAGE_BASE_URL + currentItem.poster_path
        holder.binding.apply {
            Glide
                .with(root)
                .load(posterLink)
                .into(ivMoviePoster)

            tvMovieName.text = currentItem.title
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}