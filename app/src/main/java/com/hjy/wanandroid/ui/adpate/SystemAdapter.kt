package com.hjy.wanandroid.ui.adpate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hjy.wanandroid.R
import com.hjy.wanandroid.mode.bean.SystemBean

class SystemAdapter(var systemBeans: ArrayList<SystemBean>,private val listener: ItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_system, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return systemBeans.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val systemHolder = holder as Holder
        val systemBean: SystemBean = systemBeans[position]
        systemHolder.titleTv.text = systemBean.name
        systemHolder.infoTv.text = StringBuffer().run {
            for (child in systemBean.children) {
                append(child.name + "\t\t")
            }
            toString()
        }
        systemHolder.itemView.setOnClickListener{
            listener.onItemClick(it,position)
        }
    }


    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv: TextView = view.findViewById(R.id.system_title_tv)
        val infoTv: TextView = view.findViewById(R.id.system_info_tv)
    }


    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}