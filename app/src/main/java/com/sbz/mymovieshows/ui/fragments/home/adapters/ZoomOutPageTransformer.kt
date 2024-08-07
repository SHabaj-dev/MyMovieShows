package com.sbz.mymovieshows.ui.fragments.home.adapters

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val r: Float = 1 - abs(position)
        page.scaleY = (0.85f + r * 0.14f)
    }
}