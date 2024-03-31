package com.hjy.wanandroid.mode.viewMode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewMode : ViewModel() {
    protected val mError: MutableLiveData<String> =
        MutableLiveData<String>()
    val _Error: LiveData<String>
        get() = mError

    //ui
    protected val mUiModel: MutableLiveData<UiModel> = MutableLiveData<UiModel>()
    val _UiModel: LiveData<UiModel>
        get() = mUiModel

    data class UiModel(
        val loading: Boolean, //加载
        val error: String?, //错误
        val end: Boolean  //下一页
    ) {
    }
}