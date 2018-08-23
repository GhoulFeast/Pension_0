package com.overwork.pension.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aisino.tool.system.dip2px
import com.aisino.tool.toast
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.fragment.*
import com.overwork.pension.other.*

class TodayTaskAdapter(val activity: FragmentActivity, val taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {

    var showTime=""

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_today_task_item, null)
        var name = view.findViewById<TextView>(R.id.item_todaytask_name)
        var room = view.findViewById<TextView>(R.id.item_todaytask_room)
        var task = view.findViewById<TextView>(R.id.item_todaytask_task)
        var state = view.findViewById<TextView>(R.id.item_todaytask_state)
        var add = view.findViewById<TextView>(R.id.item_todaytask_add)
        name.setText(taskList[p0]["name"].toString())
        room.setText(taskList[p0]["wardNumber"].toString())
        task.setText(taskList[p0]["task"].toString())
        if (taskList[p0]["state"].toString() .equals("Y") ) {
            state.background = activity.resources?.getDrawable(R.drawable.text_green_raid)
            state.setTextColor(activity.resources?.getColor(R.color.white)!!)
            state.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))
            state.setOnClickListener {
                "已完成任务".toast(activity)
            }
        } else {
            state.background = activity.resources?.getDrawable(R.drawable.border_white)
            state.setTextColor(activity.resources?.getColor(R.color.mainColor)!!)
            state.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))

        }
//        name.setOnClickListener {
//            var old = OldInfoFragment()
//            var bd = Bundle();
//            bd.putString("id", taskList[p0]["rwid"].toString())
//            old.arguments = bd
//            (activity as MenuActivity).showFragment(old)
//
//
//        }
        state.setOnClickListener{
            jump(p0)

        }
        add.setOnClickListener {
            jump(p0)

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
        return taskList.size
    }

    fun jump(index: Int): Unit {
        var taskDetailsFragment = TaskDetailsFragment();
        var bd = Bundle()
        bd.putString("time", showTime)
        taskDetailsFragment.arguments = bd
        (activity as MenuActivity).putData(TodayTaskID, taskList[index]["rwid"]!!)
        (activity as MenuActivity).putData(lrId, taskList[index]["lrid"]!!)
        (activity as MenuActivity).putData(zbpkId, taskList[index]["zbpkid"]!!)
        if (userType.equals("1")){
            CZLX="01"
        }else{
            CZLX="03"
        }
        (activity as MenuActivity).showFragment(taskDetailsFragment)
    }
}