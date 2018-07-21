package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.OVER_TASK
import com.overwork.pension.other.userId

class SmallTaskAdapter(activity: FragmentActivity, taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter(){
    private var list: List<MutableMap<String,Any>>? = null
    private var context: Context? = null

    init {
        this.list = taskList
        this.context = activity
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_small_task, null)
        var task = view.findViewById<TextView>(R.id.item_small_task)
        var complete = view.findViewById<TextView>(R.id.item_small_complete)

        task.setText(list!![p0]["name"].toString())
        complete.setOnClickListener {
            Http.get{
                url= BASEURL + OVER_TASK
                "userId"- userId
                "id"-list!![p0]["id"].toString()
                success {
                    if (!"status"=="2000"){
                        ToastAdd.showToast_r(context!!,"已完成任务")
                    }
                }
            }
        }

        return view
    }

    override fun getItem(p0: Int): Any {
       return  p0
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getCount(): Int {
        return list!!.size
    }
}