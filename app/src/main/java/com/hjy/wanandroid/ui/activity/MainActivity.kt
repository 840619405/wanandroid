package com.hjy.wanandroid.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.ActivityBase
import com.hjy.wanandroid.ui.fragment.ArticleTabFragment

class MainActivity : ActivityBase() {
    private val TAG = "MainActivity";
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view);
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.blogFragment -> {
                    val bundle: Bundle = Bundle().apply {
                        putInt(
                            ArticleTabFragment.TAB_TYPE,
                            ArticleTabFragment.PARAM_TAB_TYPE_BLOG
                        )
                        putBoolean(ArticleTabFragment.IS_NV_SHOW, false)
                    }
                    navController.navigate(item.itemId, bundle)
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }


    override fun initData() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}