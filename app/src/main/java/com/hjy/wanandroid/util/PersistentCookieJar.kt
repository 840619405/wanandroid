package com.hjy.wanandroid.util

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hjy.wanandroid.filter.get
import com.hjy.wanandroid.filter.put
import com.hjy.wanandroid.MyApplication
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class PersistentCookieJar : CookieJar {
    private val TAG = "PersistentCookieJar"
    private val COOKIE = "COOKIE"
    private val mSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.context)
    }
    private val mSharedPreferencesEdit: SharedPreferences.Editor by lazy {
        mSharedPreferences.edit()
    }
//    private val mSharedPreferences: SharedPreferences by lazy {
//        PreferenceManager.getDefaultSharedPreferences(MyApplication.context)
//    }


    //cookies
    private var cookies: MutableList<Cookie>? = null

    //是否更新cookies
    private var isUpdate: Boolean = false

    private val gson: Gson by lazy {
        Gson()
    }

    //  private val test: Boolean by lazy { false }
    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        var isClear: Boolean = false
        var isSave: Boolean = false
        for (cookie in cookies) {
            //判断是否清空cookie
            if (cookie.name() == "max-Age" && cookie.value().toInt() == 0) {
                isClear = true
            }
            //判断是否是登录成功的cookie
            if (cookie.name() == "loginUserName") {
                isSave = true
            }
        }
        if (isSave) {
            mSharedPreferencesEdit.put(COOKIE, gson.toJson(cookies))
            this.cookies = cookies
        }
        if (isClear) {
            mSharedPreferencesEdit.remove(COOKIE).commit()
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie>? {
        return if (cookies != null) {
            cookies
        } else {
            val data: String = mSharedPreferences.get(COOKIE, "") as String
            if (data.isNotEmpty()) {
                val typeToken = object : TypeToken<MutableList<Cookie>>() {}.type
                gson.fromJson(data, typeToken)
            } else {
                mutableListOf()
            }
        }
    }
}

