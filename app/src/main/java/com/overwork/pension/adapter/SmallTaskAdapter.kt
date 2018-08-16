package com.overwork.pension.adapter

import android.content.Context
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
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.OVER_TASK
import com.overwork.pension.other.userId

class SmallTaskAdapter(val activity: FragmentActivity,val taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter(){


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(activity).inflate(R.layout.item_small_task, null)
        var task = view.findViewById<TextView>(R.id.item_small_task)
        var complete = view.findViewById<TextView>(R.id.item_small_complete)

        task.setText(taskList[p0]["name"].toString())
        if (taskList[p0]["isComplete"].toString() .equals("Y") ) {//是否完成
            complete.background = activity.resources?.getDrawable(R.drawable.text_green_raid)
            complete.setTextColor(activity.resources?.getColor(R.color.white)!!)
            complete.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))
            complete.setOnClickListener {
                "已完成任务".toast(activity)
            }
        } else {
            if (taskList[p0]["isNecessary"].toString() .equals("Y")){//是否必要
                complete.background = activity.resources?.getDrawable(R.drawable.border_red)
                complete.setTextColor(activity.resources?.getColor(R.color.white)!!)
                complete.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))
            }else{
                complete.background = activity.resources?.getDrawable(R.drawable.border_white)
                complete.setTextColor(activity.resources?.getColor(R.color.mainColor)!!)
                complete.setPadding(activity.dip2px(24F), activity.dip2px(5F), activity.dip2px(24F), activity.dip2px(5F))
            }
            complete.setOnClickListener {
                Http.get{
                    url= BASEURL + OVER_TASK
                    "userId"- userId
                    "zhrwId"-taskList[p0]["zhid"].toString()
                    success {
                        if ("status".equals("200")){
                            "已完成任务".toast(activity)
                        }else{
                            getAny<String>("message").toast(activity)
                        }
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
        return taskList.size
    }
}

