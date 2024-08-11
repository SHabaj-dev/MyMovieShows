package com.sbz.mymovieshows.ui.activitys.movieInfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sbz.mymovieshows.R
import com.sbz.mymovieshows.databinding.RvItemCastBinding
import com.sbz.mymovieshows.models.Cast
import com.sbz.mymovieshows.utils.Constants.IMAGE_BASE_URL

class CastAdapter : RecyclerView.Adapter<CastAdapter.ViewHolder>() {


    private val differCallBack = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)


    inner class ViewHolder(val binding: RvItemCastBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        val imageUrl = IMAGE_BASE_URL + currentItem.profile_path
        val placeHolder = ContextCompat.getDrawable(
            holder.binding.root.context,
            if (currentItem.gender == 1) R.drawable.ic_female_place_holder else R.drawable.ic_male_place_holder
        )

        val genderColor = ContextCompat.getColor(
            holder.binding.root.context,
            if (currentItem.gender == 1) R.color.female else R.color.male
        )

        holder.binding.apply {
            tvName.text = currentItem.name
            tvRole.text = currentItem.character
            cvInfo.strokeColor = genderColor
            tvRole.isSelected = true
            Glide.with(root).load(imageUrl).placeholder(placeHolder).into(ivImagePlaceHolder)

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}