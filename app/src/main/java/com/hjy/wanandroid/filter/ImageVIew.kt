package com.hjy.wanandroid.filter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hjy.wanandroid.R

fun ImageView.load(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .thumbnail(0.2f)
        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}