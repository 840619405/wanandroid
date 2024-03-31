package com.hjy.wanandroid.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hjy.wanandroid.R

abstract class ActivityBase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView();
        initData();
    }

    abstract fun initView()
    abstract fun initData()
    abstract fun getLayoutResId(): Int

}