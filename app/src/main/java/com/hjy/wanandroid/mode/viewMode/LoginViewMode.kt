package com.hjy.wanandroid.mode.viewMode

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjy.wanandroid.R
import com.hjy.wanandroid.mode.bean.UserInfoBean
import com.hjy.wanandroid.mode.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewMode : BaseViewMode() {
    companion object {
        const val TYPE_LOGIN: Int = 1 //登录
        const val TYPE_LOGIN_REGISTER: Int = 2 //注册并登录
    }

    private val mLoginForm = MutableLiveData<LoginFormState>()
    val _mLoginForm: LiveData<LoginFormState> = mLoginForm
    private val mWanRepository: WanRepository = WanRepository()
    private val mUserInfo: MutableLiveData<UserInfoBean> = MutableLiveData<UserInfoBean>()
    val _UserInfo: LiveData<UserInfoBean>
        get() = mUserInfo
    private val mLoginState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val _LoginState: LiveData<Boolean>
        get() = mLoginState

    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                mWanRepository.getProjectList()
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mLoginState.value = true
                mUserInfo.value = result.data?.userInfo
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                if (result.errCode == -1001) {
                    mLoginState.value = false
                } else {
                    mError.value = result.exception.message
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            if (password.isNotEmpty()) {
                mLoginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            }
        } else if (!isPasswordValid(password)) {
            if (password.isNotEmpty()) {
                mLoginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            }
        } else {
            mLoginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun login(username: String, password: String, type: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                when (type) {
                    TYPE_LOGIN ->
                        mWanRepository.login(username, password)
                    TYPE_LOGIN_REGISTER ->
                        mWanRepository.register(username, password)
                    else -> com.hjy.wanandroid.mode.api.Result.Error(java.lang.Exception("登录类型错误"))
                }
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mLoginState.value = true
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mError.value = result.exception.message
            }
        }
    }

    fun loginOut() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                mWanRepository.loginOut()
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mLoginState.value = false
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mLoginState.value = false
            }
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return isPasswordValid(username)
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


    data class LoginFormState(
        val usernameError: Int? = null,
        val passwordError: Int? = null,
        val isDataValid: Boolean = false
    )
}