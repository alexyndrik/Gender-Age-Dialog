package com.example.myapplication.ui

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextCustomStyle")
    fun TextView.setTextCustomStyle(isBold :Boolean){
        if (isBold) this.setTypeface(this.typeface, Typeface.BOLD_ITALIC)
        else this.setTypeface(this.typeface, Typeface.ITALIC)
    }

    @JvmStatic
    @BindingAdapter("setCustomVisibility")
    fun View.setCustomVisibility(isVisible :Boolean){
        this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

}