package com.overwork.pension.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
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
        var item_isread = view.findViewById<View>(R.id.item_isread)
        if(list.get(p0).get("isRead").toString().toInt()==1){
            item_isread.visibility=View.INVISIBLE
        }else{
            item_isread.visibility=View.VISIBLE
        }
        if (list.get(p0)["type"].toString().toInt() == 0) {
            job.setText("护")
            jobText.setText(list.get(p0).get("tasks").toString().replace("||",";"))
        } else {
            job.setText("通")
            jobText.setText(list.get(p0).get("messageContent").toString())
        }
        jobTitle.setText(list.get(p0).get("name").toString()+"\t"+list.get(p0).get("kssj").toString()+"-"+list.get(p0).get("jssj").toString()+list.get(p0).get("messageTitle").toString())

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