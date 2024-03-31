package com.hjy.wanandroid.mode.viewMode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjy.wanandroid.mode.bean.SystemBean
import com.hjy.wanandroid.mode.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WxArticleTabViewMode : BaseViewMode() {
    private val mWxArticleTab: MutableLiveData<List<SystemBean>> = MutableLiveData<List<SystemBean>>()
    val _WxArticleTab: LiveData<List<SystemBean>>
        get() = mWxArticleTab

    private val mUiMode: MutableLiveData<SystemViewMode.UiMode> = MutableLiveData<SystemViewMode.UiMode>()
    val _uiMode: LiveData<SystemViewMode.UiMode>
        get() = mUiMode

    private val mArticleRepository: WanRepository = WanRepository()


    fun getWxArticleTab(){
        viewModelScope.launch(Dispatchers.Main) {
            var result = withContext(Dispatchers.IO) {
                mArticleRepository.getWxArticleTab()
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mWxArticleTab.value = result.data
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mUiMode.value = SystemViewMode.UiMode(false, result.exception.message)
            }
        }
    }


    class UiMode(val loading: Boolean, val error: String?) {

    }
}