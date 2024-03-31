package com.hjy.wanandroid.base

import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

abstract class FragmentBase : Fragment() {
    var TAG_BASE="FragmentBase"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG_BASE, "onViewCreated: ")
        initView(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG_BASE, "onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_BASE, "onCreate: ")
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG_BASE, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        initData();
        Log.d(TAG_BASE, "onResume: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG_BASE, "onDestroyView: ")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_BASE, "onDestroy: ")
    }
    abstract fun initView(view: View)
    abstract fun initData()
}