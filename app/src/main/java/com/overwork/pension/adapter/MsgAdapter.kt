package com.overwork.pension.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.overwork.pension.R

class MsgAdapter(context: Context, list: List<MsgM>) : BaseAdapter() {
    private var list: List<MsgM>? = null
    private var context: Context? = null

    init {
        this.list = list
        this.context = context
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.fragment_msg_item, p2)
        var job = view.findViewById<TextView>(R.id.item_job)
        var jobTitle = view.findViewById<TextView>(R.id.item_job_title)
        var jobText = view.findViewById<TextView>(R.id.item_job_text)
        job.setText(list!![p0].job)
        jobTitle.setText(list!![p0].title)
        jobText.setText(list!![p0].text )
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list?.size!!
    }

    class MsgM {
        var job = "护"
        var title = "张三1"
        var text = "工作中"
    }
}