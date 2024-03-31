package com.hjy.wanandroid.mode.viewMode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjy.wanandroid.mode.api.Result
import com.hjy.wanandroid.mode.bean.FriendBean
import com.hjy.wanandroid.mode.repository.WanRepository
import kotlinx.coroutines.*

class FriendViewMode : BaseViewMode() {
    private val mFriend: MutableLiveData<ArrayList<FriendBean>> =
        MutableLiveData<ArrayList<FriendBean>>()
    val _Friend: LiveData<ArrayList<FriendBean>>
        get() = mFriend



    private val mWanRepository: WanRepository = WanRepository()

    fun getFriend() {
        viewModelScope.launch(Dispatchers.Main) {
            val friendsResult = async(Dispatchers.IO) {
                mWanRepository.getFriends()
            }
            val hotkeyResult = async(Dispatchers.IO) {
                mWanRepository.getHotkey()
            }
            val await: Result<List<FriendBean>> = friendsResult.await()
            val await2: Result<List<FriendBean>> = hotkeyResult.await()
            if (await is Result.Success && await2 is Result.Success) {
                val result: ArrayList<FriendBean> = arrayListOf<FriendBean>().apply {
                    //热词
                    await2.data?.let { addAll(it) }

                    //网站
                    await.data?.let { addAll(it) }
                }
                mFriend.value = result
            } else {
                mError.value = "热词加载失败，请直接搜索"
            }
        }
    }
}