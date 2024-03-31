package com.hjy.wanandroid.ui.adpate

import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hjy.wanandroid.R
import com.hjy.wanandroid.mode.bean.ArticleListBean

class ArticleListAdapter(
    var articleList: ArrayList<ArticleListBean.Article>,
    val itemListener: ItemClickListener,
    var collectListener: OnCollectClickListener? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG: String = "ArticleListAdapter"

    companion object {
        const val ARTICLE_TYPE: Int = 1
        const val LOADINT_TYPE: Int = 2
    }

    var isLastPage: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ARTICLE_TYPE)
            ArticleHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
            )
        else
            LoadingHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_load, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleHolder -> {
                var data = articleList[position]
                if (TextUtils.isEmpty(data.author)) {
                    holder.articleAuthorTv.text = "分享者: ${data.shareUser}"
                } else {
                    holder.articleAuthorTv.text = data.author
                }
                val richText: CharSequence =
                    Html.fromHtml(data.title)
                holder.articleTitleTv.text = richText
                holder.articleSourceTv.text = data.superChapterName + "/" + data.chapterName
                holder.articleTimeTv.text = data.niceDate
                holder.itemView.setOnClickListener() {
                    itemListener.onItemClick(holder.itemView, position)
                }
                if (data.collect) {
                    holder.articleCollectIv.setImageResource(R.drawable.timeline_like_pressed)
                } else {
                    holder.articleCollectIv.setImageResource(R.drawable.timeline_like_normal)
                }
                holder.articleCollectIv.setOnClickListener {
                    collectListener?.let {
                        it.onClick(holder.articleCollectIv, data.id, position, data.collect)
                    }
                }
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
        if (articleList.size == 0) {
            isLastPage = true
            return 1
        } else {
            return articleList.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == articleList.size)
            LOADINT_TYPE
        else
            ARTICLE_TYPE
    }

    class ArticleHolder(view: View) : RecyclerView.ViewHolder(view) {
        val articleAuthorTv: TextView = view.findViewById(R.id.article_author_tv)
        val articleSourceTv: TextView = view.findViewById(R.id.article_source_tv)
        val articleTitleTv: TextView = view.findViewById(R.id.article_title_tv)
        val articleTimeTv: TextView = view.findViewById(R.id.article_time_tv)
        val articleCollectIv: ImageView = view.findViewById(R.id.article_collect_iv)
    }

    class LoadingHolder(view: View) : RecyclerView.ViewHolder(view) {
        var loadEndTv: TextView = view.findViewById(R.id.load_end_tv)
        val progressBar: ProgressBar = view.findViewById(R.id.load_proBar)
    }


    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    /**
     * 收藏文章监听接口
     */
    interface OnCollectClickListener {

        fun onClick(view: View, cid: Int, position: Int, collect: Boolean)
    }
}