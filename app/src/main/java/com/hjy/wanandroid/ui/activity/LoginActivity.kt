package com.hjy.wanandroid.ui.activity

import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.hjy.wanandroid.filter.afterTextChanged
import com.hjy.wanandroid.filter.getString
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.ActivityBase
import com.hjy.wanandroid.mode.viewMode.LoginViewMode

class LoginActivity : ActivityBase() {
    private lateinit var loginTypeTv: TextView
    private lateinit var mLoginBtn: Button
    private lateinit var mUsernameEt: EditText
    private lateinit var mPasswordEt: EditText
    private lateinit var mLoading: ProgressBar
    private lateinit var mViewMode: LoginViewMode
    private var type: Int = LoginViewMode.TYPE_LOGIN
    override fun initView() {
        initActionBar()
        mViewMode = ViewModelProvider(this)[LoginViewMode::class.java]
        loginTypeTv = findViewById(R.id.login_type_tv)
        mLoginBtn = findViewById(R.id.login_btn)
        mUsernameEt = findViewById(R.id.login_username_tv)
        mPasswordEt = findViewById(R.id.login_password_tv)
        mLoading = findViewById(R.id.login_loading)
        loginTypeTv.setOnClickListener {
            when (type) {
                LoginViewMode.TYPE_LOGIN ->
                    type = LoginViewMode.TYPE_LOGIN_REGISTER
                LoginViewMode.TYPE_LOGIN_REGISTER ->
                    type = LoginViewMode.TYPE_LOGIN
                else -> {

                }
            }
            initLoginType()
        }
        mUsernameEt.afterTextChanged {
            mViewMode.loginDataChanged(
                mUsernameEt.text.toString(),
                mPasswordEt.text.toString()
            )
        }
        mPasswordEt.afterTextChanged {
            mViewMode.loginDataChanged(
                mUsernameEt.text.toString(),
                mPasswordEt.text.toString()
            )
        }
        mLoginBtn.setOnClickListener {
            mLoading.visibility = View.VISIBLE
            mViewMode.login(mUsernameEt.text.toString(), mPasswordEt.text.toString(), type)
        }
        initLoginType()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 根据登录类型设置控件文字
     */
    private fun initLoginType() {
        when (type) {
            LoginViewMode.TYPE_LOGIN -> {
                loginTypeTv.setText(R.string.login_and_register)
                mLoginBtn.setText(R.string.login)
            }

            LoginViewMode.TYPE_LOGIN_REGISTER -> {
                loginTypeTv.setText(R.string.login_type_login)
                mLoginBtn.setText(R.string.login_and_register)
            }
            else -> {

            }
        }
    }

    override fun initData() {
        mViewMode.apply {
            _mLoginForm.observe(this@LoginActivity) {
                mLoginBtn.isEnabled = it.isDataValid
                if (it.passwordError != null) {
                    mPasswordEt.error = it.passwordError.getString()
                }
                if (it.usernameError != null) {
                    mUsernameEt.error = it.usernameError.getString()
                }
            }

            _LoginState.observe(this@LoginActivity) {
                if (it) {
                    "登录成功".toast(this@LoginActivity)
                    finish()
                }
            }

            _Error.observe(this@LoginActivity) {
                mLoading.visibility = View.GONE
                it.toast(this@LoginActivity)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }
}