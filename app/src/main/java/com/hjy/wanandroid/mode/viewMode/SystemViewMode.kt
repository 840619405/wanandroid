package com.hjy.wanandroid.mode.viewMode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjy.wanandroid.mode.bean.SystemBean
import com.hjy.wanandroid.mode.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SystemViewMode : BaseViewMode() {
    private val mSystemRepository: WanRepository = WanRepository()
    private val TAG: String = "SystemViewMode"
    private val mSystemMode: MutableLiveData<List<SystemBean>> = MutableLiveData<List<SystemBean>>()
    val _System: LiveData<List<SystemBean>>
        get() = mSystemMode
    private val mUiMode: MutableLiveData<UiMode> = MutableLiveData<UiMode>()
    val _uiMode: LiveData<UiMode>
        get() = mUiMode

    fun getSystemList() {
        viewModelScope.launch {
            mUiMode.value = UiMode(true, null)
            var result = withContext(Dispatchers.IO) {
                mSystemRepository.getSystemTypeArticles()
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mSystemMode.value = result.data
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mUiMode.value = UiMode(false, result.exception.message)
            }
        }
    }

    class UiMode(val loading: Boolean, val error: String?) {

    }
}