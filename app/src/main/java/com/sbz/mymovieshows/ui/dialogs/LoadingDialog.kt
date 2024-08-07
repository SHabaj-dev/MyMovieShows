package com.sbz.mymovieshows.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.sbz.mymovieshows.R
import com.sbz.mymovieshows.databinding.DialogLoadingBinding

class LoadingDialog(private val context: Context) : Dialog(context) {

    private lateinit var binding: DialogLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
//        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.statusBarColor = ContextCompat.getColor(context, R.color.black)
        setCancelable(false)
    }

}