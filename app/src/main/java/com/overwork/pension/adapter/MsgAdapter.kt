package com.overwork.pension.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.overwork.pension.R

class MsgAdapter(context: Context, list: List<MutableMap<String, Any>>) : BaseAdapter() {
    private var list: List<MutableMap<String, Any>>
    private var context: Context? = null

    init {
        this.list = list
        this.context = context
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.fragment_msg_item, null)
        var job = view.findViewById<TextView>(R.id.item_job)
        var jobTitle = view.findViewById<TextView>(R.id.item_job_title)
        var jobText = view.findViewById<TextView>(R.id.item_job_text)
        if (list.get(p0).get("type") == 1) {
            job.setText("护")
        } else {
            job.setText("通")
        }
        jobTitle.setText(list.get(p0).get("messageTitle").toString())
        jobText.setText(list.get(p0).get("messageContent").toString())
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


}