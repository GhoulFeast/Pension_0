package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.overwork.pension.R

class TodayTaskAdapter(activity: FragmentActivity, taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter(){
    private var list: List<MutableMap<String,Any>>? = null
    private var context: Context? = null

    init {
        this.list = list
        this.context = context
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.fragment_today_task_item, p2)
        var name = view.findViewById<TextView>(R.id.item_todaytask_name)
        var room = view.findViewById<TextView>(R.id.item_todaytask_room)
        var task = view.findViewById<TextView>(R.id.item_todaytask_task)
        var state = view.findViewById<TextView>(R.id.item_todaytask_state)
        var add = view.findViewById<TextView>(R.id.item_todaytask_add)
        name.setText(list!![p0]["name"].toString())
        room.setText(list!![p0]["wardNumber "].toString())
        task.setText(list!![p0]["task"].toString() )
        if (list!![p0]["wardNumber "].toString()=="0"){
            state.background=context?.resources?.getDrawable(R.drawable.text_green_raid)
        }else{
            state.background=context?.resources?.getDrawable(R.drawable.border_white)
        }
        state.setOnClickListener {

        }
        add.setOnClickListener {

        }
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