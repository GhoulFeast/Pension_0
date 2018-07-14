package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.aisino.tool.log
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.TodayTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_today_task.*

val TodayTaskID="TodayTaskID"

class TodayTaskFragment : Fragment(){
    var time=""
    lateinit var taskList:ArrayList<MutableMap<String,Any>>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_today_task, container, false)

        Http.get{
            url= BASEURL+T_TASK
            "userId"- userId
            success {
                time= "result".."time"
                taskList="result".."links"
                todaytask_list.adapter= TodayTaskAdapter(activity,taskList)
            }
        }

        (activity as MenuActivity).style {
            textBar=""
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskList= ArrayList<MutableMap<String,Any>>()
        val c:MutableMap<String,Any> = mutableMapOf()
        c.put("name","张三")
        c.put("wardNumber","301")
        taskList.add(c)

        todaytask_list.adapter= TodayTaskAdapter(activity,taskList)
        todaytask_list.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            (activity as MenuActivity).showFragment(TaskDetailsFragment())
            (activity as MenuActivity).putData(TodayTaskID,taskList[i]["id"]!!)
        }

    }
}