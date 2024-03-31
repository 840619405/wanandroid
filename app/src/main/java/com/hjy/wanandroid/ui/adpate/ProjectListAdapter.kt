package com.hjy.wanandroid.ui.adpate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjy.wanandroid.filter.load
import com.hjy.wanandroid.R
import com.hjy.wanandroid.mode.bean.ProjectBean

class ProjectListAdapter(
    var projectList: ArrayList<ProjectBean>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val PROJECT_TYPE: Int = 1
        const val LOADINT_TYPE: Int = 2
    }

    var isLastPage: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PROJECT_TYPE)
            ProjectHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
            )
        else
            LoadingHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_load, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProjectHolder -> {
                val projectBean: ProjectBean = projectList[position]
                holder.projectTitle.text = projectBean.title
                holder.projectTime.text = projectBean.niceDate
                holder.projectDesc.text = projectBean.desc
                holder.itemView.setOnClickListener {
                    listener.onItemClick(holder.itemView, position)
                }
                holder.projectIcon.load(holder.itemView.context, projectBean.envelopePic)
            }
            is LoadingHolder -> {
                if (position == 0) {
                    holder.itemView.visibility = View.GONE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                }
                if (isLastPage) {
                    holder.progressBar.visibility = View.GONE
                    holder.loadEndTv.visibility = View.VISIBLE
                } else {
                    holder.progressBar.visibility = View.VISIBLE
                    holder.loadEndTv.visibility = View.GONE
                }
            }
            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        if (projectList.size == 0) {
            isLastPage = true
            return 1
        } else {
            return projectList.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == projectList.size)
            ArticleListAdapter.LOADINT_TYPE
        else
            ArticleListAdapter.ARTICLE_TYPE
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ProjectHolder) {
            Glide.with(holder.itemView.context).clear(holder.projectIcon)
        }
    }


    class LoadingHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loadEndTv: TextView = view.findViewById(R.id.load_end_tv)
        val progressBar: ProgressBar = view.findViewById(R.id.load_proBar)
    }

    class ProjectHolder(view: View) : RecyclerView.ViewHolder(view) {
        val projectIcon: ImageView = view.findViewById(R.id.project_iv)
        val projectTitle: TextView = view.findViewById(R.id.project_title_tv)
        val projectDesc: TextView = view.findViewById(R.id.project_desc_tv)
        val projectTime: TextView = view.findViewById(R.id.project_time_tv)
    }


    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}