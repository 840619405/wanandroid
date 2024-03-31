package com.hjy.wanandroid.mode.viewMode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjy.wanandroid.mode.bean.ProjectBean
import com.hjy.wanandroid.mode.bean.ProjectTypeBean
import com.hjy.wanandroid.mode.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewMode : BaseViewMode() {
    private var page = -1
    private val mProjectTypes: MutableLiveData<List<ProjectTypeBean>> = MutableLiveData()
    val _ProjectTypes: LiveData<List<ProjectTypeBean>>
        get() = mProjectTypes

    private val mProjectList: MutableLiveData<List<ProjectBean>> = MutableLiveData()
    val _ProjectList: LiveData<List<ProjectBean>>
        get() = mProjectList

    private val wanRepository = WanRepository()


    fun getProjectTypes() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                wanRepository.getProjectType()
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mProjectTypes.value = result.data!!
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mError.value = result.exception.message
            }
        }
    }

    fun getProjectListTypes(refetch: Boolean, cid: Int) {
        if (refetch)
            page = -1
        page++
        mUiModel.value = UiModel(refetch, null, false)
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                wanRepository.getProjectList(page, cid)
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mProjectList.value = result.data?.datas
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mUiModel.value = UiModel(false, result.exception.message, false)
            }
        }
    }
}