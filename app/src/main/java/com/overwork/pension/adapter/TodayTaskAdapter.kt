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
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.fragment.OldInfoFragment
import com.overwork.pension.fragment.TaskDetailsFragment
import com.overwork.pension.other.*

class TodayTaskAdapter(val activity: FragmentActivity, val taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {

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
        if (taskList[p0]["state"].toString() == "0") {
            state.background = activity.resources?.getDrawable(R.drawable.text_green_raid)
            state.setTextColor(activity.resources?.getColor(R.color.white)!!)
            state.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))
            state.setOnClickListener {
                ToastAdd.showToast_r(activity, "已完成任务")
            }
        } else {
            state.background = activity.resources?.getDrawable(R.drawable.border_white)
            state.setTextColor(activity.resources?.getColor(R.color.mainColor)!!)
            state.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))

        }
        name.setOnClickListener {
            var old = OldInfoFragment()
            var bd = Bundle();
            bd.putString("id", taskList[p0]["id"].toString())
            old.arguments = bd
            (activity as MenuActivity).showFragment(old)


        }
        add.setOnClickListener {
            (activity as MenuActivity).showFragment(TaskDetailsFragment())
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


}