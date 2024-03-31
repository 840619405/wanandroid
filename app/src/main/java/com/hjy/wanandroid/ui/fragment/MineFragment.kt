package com.hjy.wanandroid.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.hjy.wanandroid.filter.getString
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.viewMode.LoginViewMode

class MineFragment : FragmentBase() {
    private lateinit var mLoginOutBtn: Button
    private lateinit var mUserIconIv: ImageView
    private lateinit var mUserNicknameTv: TextView
    private lateinit var mViewMode: LoginViewMode
    private lateinit var mNav: NavigationView
    private var isLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView(view: View) {
        mViewMode = ViewModelProvider(this).get(LoginViewMode::class.java)
        mNav = view.findViewById(R.id.mine_nav)
        mNav.setNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.menu_open_source -> {
                    val url = R.string.open_source_url.getString()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
                else -> {

                }
            }
            return@setNavigationItemSelectedListener false
        }
        mLoginOutBtn = view.findViewById(R.id.login_out_btn)
        mUserIconIv = view.findViewById(R.id.user_icon_iv)
        mUserNicknameTv = view.findViewById(R.id.user_nice_tv)
        mUserIconIv.setOnClickListener {
            if (!isLogin) {
                findNavController().navigate(R.id.loginActivity)
            }
        }
        mLoginOutBtn.setOnClickListener {
            mViewMode.loginOut()
        }
    }

    override fun initData() {
        mViewMode.apply {
            _LoginState.observe(this@MineFragment) {
                isLogin = it
                //是否登录
                if (it) {
                    mLoginOutBtn.visibility = View.VISIBLE
                    mUserNicknameTv.visibility = View.VISIBLE
                } else {
                    mLoginOutBtn.visibility = View.GONE
                    mUserNicknameTv.visibility = View.GONE
                }
            }
            _UserInfo.observe(this@MineFragment) {
                mUserNicknameTv.text = it.nickname
            }
        }.getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }
}