package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.aisino.tool.log
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.TaskStepViewRvAdapter
import com.overwork.pension.adapter.TodayTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_today_task.*
import java.util.*

val TodayTaskID = "TodayTaskID"

class TodayTaskFragment : Fragment() {
    var time = ""
    var showTime = ""
    var taskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var thisTaskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    lateinit var todayTaskAdapter: TodayTaskAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_today_task, container, false)
        Http.get {
            url = BASEURL + T_TASK
            "userId" - userId
            success {
                time = "result".."time"
                taskList.clear()
                taskList .addAll( "result".."timeTasks")
                for (mut: MutableMap<String, Any> in taskList) {
                    if (mut["taskState"].toString().toInt()== 3) {
                        showTime = mut["taskTime"].toString()
                        thisTaskList.clear()
                        thisTaskList.addAll(mut["links"] as ArrayList<MutableMap<String, Any>>)
                    }
                }
                activity.runOnUiThread{
                    todayTaskAdapter.notifyDataSetChanged()
                    taskStepViewRvAdapter.notifyDataSetChanged()
                }
            }
        }
        (activity as MenuActivity).style {
            textBar = ""
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayTaskAdapter = TodayTaskAdapter(activity, thisTaskList)
        taskStepViewRvAdapter = TaskStepViewRvAdapter(activity, taskList)
        todaytask_rv.adapter = taskStepViewRvAdapter;
        todaytask_list.adapter = todayTaskAdapter
        todaytask_list.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            var taskDetailsFragment = TaskDetailsFragment();
            var bd = Bundle()
            bd.putString("time", showTime)
            taskDetailsFragment.arguments = bd
            (activity as MenuActivity).showFragment(taskDetailsFragment)
            (activity as MenuActivity).putData(TodayTaskID, taskList[i]["id"]!!)
        }
        taskStepViewRvAdapter.setStepItemClick(object : TaskStepViewRvAdapter.TaskStepItemClick {
            override fun OnItem(postion: Int) {
                showTime = taskList.get(postion)["taskTime"].toString()
                thisTaskList.clear()
                thisTaskList.addAll(taskList.get(postion)["links"] as ArrayList<MutableMap<String, Any>>)
                todayTaskAdapter.notifyDataSetChanged()
            }
        })
    }

}