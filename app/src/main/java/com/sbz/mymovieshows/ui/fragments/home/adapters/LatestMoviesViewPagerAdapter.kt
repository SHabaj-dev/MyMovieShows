package com.sbz.mymovieshows.ui.fragments.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.sbz.mymovieshows.databinding.VpItemLatestMoviesBinding
import com.sbz.mymovieshows.models.Result
import com.sbz.mymovieshows.utils.Constants.IMAGE_BASE_URL

class LatestMoviesViewPagerAdapter(private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<LatestMoviesViewPagerAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(val binding: VpItemLatestMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val differCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            VpItemLatestMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        val imageUrl = IMAGE_BASE_URL + currentItem.poster_path
        holder.binding.apply {
            Glide
                .with(root)
                .load(imageUrl)
                .into(ivLatestMovies)
        }

        if (position == differ.currentList.size - 2) {
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        val currentList = differ.currentList.toMutableList()
        currentList.addAll(currentList)
        differ.submitList(currentList)
    }
}