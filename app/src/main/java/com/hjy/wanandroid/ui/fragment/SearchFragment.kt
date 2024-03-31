package com.hjy.wanandroid.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexboxLayout
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.bean.FriendBean
import com.hjy.wanandroid.mode.viewMode.ArticlesViewMode
import com.hjy.wanandroid.mode.viewMode.FriendViewMode
import com.hjy.wanandroid.ui.custom.ClearableEditText
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : FragmentBase() {
    private val TAG: String = "SearchFragment"
    private lateinit var mViewMode: FriendViewMode
    private lateinit var mFlexboxLayout: FlexboxLayout
    private lateinit var mSearchEt: ClearableEditText

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val MIN_INPUT_LENGTH: Int = 2
    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private lateinit var mArticleListFragment: ArticleListFragment;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun initView(view: View) {
        mArticleListFragment =
            childFragmentManager.findFragmentById(R.id.article_fragment) as ArticleListFragment
        mFlexboxLayout = view.findViewById<FlexboxLayout>(R.id.flexboxLayout)
        hideArticle()
        mSearchEt = view.findViewById<ClearableEditText>(R.id.search_et)
        mSearchEt.addMyTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.toString().length == 0) {
                    hideArticle()
                }
                // 判断是否满足加载数据的条件，例如输入长度大于某个值，或者检测到输入停止（例如通过Handler延时处理）
                if (s?.isNotEmpty() == true && s.length >= MIN_INPUT_LENGTH) { // MIN_INPUT_LENGTH是设定的最小输入长度
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        Log.d(TAG, "afterTextChanged: " + s.toString())
                        // 在延迟时间过后（例如1秒）如果没有新的输入，则认为输入完成
                        if (mSearchEt.text.toString() == s.toString()) {
                            Log.d(TAG, "setTypeAndCid: " + s.toString())
                            showArticle()
                            mArticleListFragment.setTypeAndSearch(
                                ArticlesViewMode.TYPE_SEARCH,
                                mSearchEt.text.toString()
                            )
                        }
                    }, 500) // DELAY_TIME是设定的延迟时间，单位是毫秒
                }
            }
        })
    }

    /**
     * 显示搜索到的文章
     */
    private fun showArticle() {
        childFragmentManager.beginTransaction().apply {
            mFlexboxLayout.visibility = View.GONE
            show(mArticleListFragment)
            commit()
        }
    }

    /**
     * 隐藏搜索文章
     */
    private fun hideArticle() {
        childFragmentManager.beginTransaction().apply {
            mFlexboxLayout.visibility = View.VISIBLE
            hide(mArticleListFragment)
            commit()
        }
    }

    override fun initData() {
        mViewMode = ViewModelProvider(this).get(FriendViewMode::class.java)
        mViewMode.apply {
            _Error.observe(this@SearchFragment) {
                context?.let { it1 -> it.toast(it1) }
            }

            _Friend.observe(this@SearchFragment) {
                loadChildView(it)
            }
            getFriend()
        }
    }

    //加载热搜词和常用网站view
    private fun loadChildView(friendBeans: ArrayList<FriendBean>) {
        //判断是否已经到网站了
        var isFriend: Boolean = false
        for (i in 0 until friendBeans.size) {
            //第一行加载热词标题
            if (i == 0) {
                mFlexboxLayout.addView(getTitleTextView("大家都在搜"))
            }
            val friendBean: FriendBean = friendBeans[i]
            //到网站了，开始加载网站标题
            if (!friendBean.link.isEmpty() && !isFriend) {
                isFriend = true
                mFlexboxLayout.addView(getTitleTextView("常用网站"))
            }
            //开始新建子view
            mFlexboxLayout.addView(
                getTextView(friendBean.name).apply {
                    setOnClickListener {
                        if (friendBean.link.isEmpty()) {
                            //搜索文章
                            mSearchEt.setText(friendBean.name)
                        } else {
                            //跳转网页
                            val url = friendBean.link // 指定的网址
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                        }
                    }
                })
        }
    }

    private fun getTitleTextView(text: String): TextView {
        val textView = TextView(context, null, R.style.TextStyle, R.style.TextStyle)
        textView.text = text
        textView.textSize = 16f
        textView.background = null
        textView.layoutParams = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.MATCH_PARENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        return textView
    }

    private fun getTextView(text: String): TextView {
        val textView = TextView(context, null, R.style.TextStyle, R.style.TextStyle)
        textView.text = text
        textView.layoutParams = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )


        return textView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}