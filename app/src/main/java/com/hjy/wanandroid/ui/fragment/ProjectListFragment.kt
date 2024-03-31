package com.hjy.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.bean.ProjectBean
import com.hjy.wanandroid.mode.viewMode.ProjectViewMode
import com.hjy.wanandroid.ui.activity.WebActivity
import com.hjy.wanandroid.ui.adpate.ProjectListAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CID = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectListFragment : FragmentBase() {
    // TODO: Rename and change types of parameters
    private var cid: Int = 0
    private lateinit var mSwipeRefLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewMode: ProjectViewMode
    private lateinit var mProjectList: ArrayList<ProjectBean>
    private lateinit var mProjectAdapter: ProjectListAdapter
    private var isLoad: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cid = it.getInt(CID)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project_list, container, false)
    }


    override fun initView(view: View) {
        mViewMode = ViewModelProvider(this).get(ProjectViewMode::class.java)
        mRecyclerView = view.findViewById(R.id.project_recycler_view)
        mSwipeRefLayout = view.findViewById(R.id.project_swipe_ref_layout)
        mSwipeRefLayout.setOnRefreshListener {
            if (mViewMode != null) {
                mProjectList.clear()
                mViewMode.getProjectListTypes(true, cid)
            }
        }
        mProjectList = arrayListOf()
        mProjectAdapter =
            ProjectListAdapter(mProjectList, object : ProjectListAdapter.ItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val intent: Intent = Intent(activity, WebActivity::class.java).apply {
                        putExtra(WebActivity.URL, mProjectList.get(position).link)
                        putExtra(WebActivity.TITEL, mProjectList.get(position).title)
                    }
                    startActivity(intent)
                }
            })

        mRecyclerView.apply {
            adapter = mProjectAdapter
            this.layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy < 0) {
                        return
                    }
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    // 设定一个距离底部的阈值，比如当剩下10个item时开始加载
                    val threshold = 1
                    if (totalItemCount <= (lastVisibleItem + threshold)) {
                        if (!isLoad && mViewMode != null) {
                            mViewMode.getProjectListTypes(false, cid)
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                }
            })
        }
    }

    override fun initData() {
        mViewMode.apply {
            _UiModel.observe(this@ProjectListFragment) {
                mSwipeRefLayout.isRefreshing = it.loading
                if (it.error != null) {
                    context?.let { it1 -> it.error.toast(it1) }
                } else {
                    //没有报错表示正在加载
                    isLoad = true
                }
            }
            _ProjectList.observe(this@ProjectListFragment) {
                mSwipeRefLayout.isRefreshing = false
                isLoad = false
                mProjectAdapter.isLastPage = it.isEmpty()
                if (it.isEmpty()) {
                    mProjectAdapter.notifyItemChanged(mProjectList.size)
                } else {
                    mProjectList.addAll(it)
                    mProjectAdapter.notifyDataSetChanged()
                }
            }
            getProjectListTypes(true, cid)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(cid: Int) =
            ProjectListFragment().apply {
                arguments = Bundle().apply {
                    putInt(CID, cid)
                }
            }
    }
}