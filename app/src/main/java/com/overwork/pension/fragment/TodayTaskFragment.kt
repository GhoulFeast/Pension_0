package com.overwork.pension.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.aisino.tool.log
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.TaskStepViewRvAdapter
import com.overwork.pension.adapter.TodayTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_today_task.*
import java.util.*
import kotlin.collections.ArrayList

val TodayTaskID = "TodayTaskID"
val lrId = "lrid"
val zbpkId = "zbpkId"
var selecttime = ""

class TodayTaskFragment : Fragment() {
    var time = ""
    var showTime = ""
    //    var taskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var taskTimeList: ArrayList<String> = ArrayList<String>()
    var thisTaskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    lateinit var linearLayoutManager: LinearLayoutManager;

    var pkid = ""
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    lateinit var todayTaskAdapter: TodayTaskAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_today_task, container, false)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = resources.getString(R.string.today_task)
        }
        if (userType.equals("2")) {//如果是主管，改变状态为主管添加
            CZLX = "03"
        }
        return view
    }

    fun intoTime() {
        Http.post {
            url = BASEURL + T_TASKTIME
            "userId" - userId
            "kssj" - showTime
            if ((activity as MenuActivity).hasData(lrId)) {
                "lrpkid" - (activity as MenuActivity).getData<String>(lrId)
            }
            if ((activity as MenuActivity).hasData("cwpkid")) {//有cwpkid时加参
                "cwpkid" - (activity as MenuActivity).getData<String>("cwpkid")
            }
            "userType" - userType
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")){
                    var times: ArrayList<MutableMap<String, Any>> = "result".."timeaxis"
                    taskTimeList.clear()
                    for (map in times) {
                        taskTimeList.add(map["kssj"].toString())
                    }
                    var position = -1;
                    var thisTime = Calendar.getInstance()
                    var tTime = thisTime.get(Calendar.HOUR_OF_DAY) * 60 + thisTime.get(Calendar.MINUTE)
                    var timelok = -1
                    var i = 0
                    while (i < taskTimeList.size) {
                        var times = taskTimeList.get(i).split(":")
                        var tTimeLok = times.get(0)?.toInt() * 60 + times.get(1)?.toInt() - tTime
                        if ((timelok == -1 || timelok > tTimeLok) && tTimeLok > 0) {
                            timelok = tTimeLok
                            position = i
                        }
                        if(i==taskTimeList.size-1){
                            if (position==-1){
                                position=taskTimeList.size-1
                            }
                        }
                        i++
                    }
                    taskStepViewRvAdapter.canSelectPosition = position
                    if (!TextUtils.isEmpty(selecttime)) {
                        position = taskTimeList.indexOf(selecttime)
                        selecttime = ""
                    }
                    taskStepViewRvAdapter.selectPosion = position
                    taskStepViewRvAdapter.notifyDataSetChanged()
                    showTime = taskTimeList.get(position)
                    todaytask_rv?.post {
                        if (taskStepViewRvAdapter.selectPosion - 2 < 0) {
                            linearLayoutManager.scrollToPositionWithOffset(0, 0)
                        } else {
                            linearLayoutManager.scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2, 0)
                        }
                    }
                    getTaskList(false)
                    }else{
                        (!"message").toast(menuActivity)
                    }
                }
            }
        }
    }

    fun getTaskList(needNext: Boolean): Unit {
        Http.post {
            url = BASEURL + T_TASK
            "userId" - userId
            "kssj" - showTime
            if ((activity as MenuActivity).hasData(lrId)) {
                "lrpkid" - (activity as MenuActivity).getData<String>(lrId)
//                (activity as MenuActivity).removeData(lrId)
            }
            if ((activity as MenuActivity).hasData("cwpkid")) {//有cwpkid时加参
                "cwpkid" - (activity as MenuActivity).getData<String>("cwpkid")
//                (activity as MenuActivity).removeData("cwpkid")
            }
            "userType" - userType
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        time = "result".."taskTime"
                        thisTaskList.clear()
                        thisTaskList.addAll("result".."links")
                    } else {
                        if (todaytask_rv != null) {
                            thisTaskList.clear()
//                            if (needNext) {//自动遍历
//                                taskStepViewRvAdapter.selectPosion = taskStepViewRvAdapter.selectPosion + 1
//                                linearLayoutManager.scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2, 0)
//                                showTime = taskTimeList.get(taskStepViewRvAdapter.selectPosion)["taskTime"].toString()
//                                taskStepViewRvAdapter.notifyDataSetChanged()
//                                if (!showTime.equals("6:30")) {
//                                    getTaskList(true)
//                                } else {
//                                    "今日已无任务".toast(menuActivity)
//                                }
//                            }
                            ((!"message").toast(menuActivity))
                        }

                    }

//                for (mut: MutableMap<String, Any> in taskList) {
//                    if (mut["taskState"].toString().toInt() == 3) {
//                        showTime = mut["taskTime"].toString()
//                        thisTaskList.clear()
//                        thisTaskList.addAll(mut["links"] as ArrayList<MutableMap<String, Any>>)
//                    }
//                }
                    todayTaskAdapter.notifyDataSetChanged()
                    taskStepViewRvAdapter.notifyDataSetChanged()
                    todayTaskAdapter.showTime = showTime
                }
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayTaskAdapter = TodayTaskAdapter(activity, thisTaskList)
//        todayTaskAdapter.isRoom = (activity as MenuActivity).hasData(lrId)
        taskStepViewRvAdapter = TaskStepViewRvAdapter(activity, taskTimeList)
//        todaytask_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        todaytask_rv.layoutManager = linearLayoutManager
        todaytask_rv.adapter = taskStepViewRvAdapter;
        todaytask_list.adapter = todayTaskAdapter
        todayTaskAdapter.showTime = showTime
//        todaytask_list.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
//            var taskDetailsFragment = TaskDetailsFragment();
//            var bd = Bundle()
//            bd.putString("time", showTime)
//            taskDetailsFragment.arguments = bd
//            (activity as MenuActivity).showFragment(taskDetailsFragment)
//            (activity as MenuActivity).putData(TodayTaskID, thisTaskList[i]["rwid"]!!)
//            (activity as MenuActivity).putData(lrId, thisTaskList[i]["lrid"]!!)
//            (activity as MenuActivity).putData(zbpkId, thisTaskList[i]["zbpkid"]!!)
//
//        }
        taskStepViewRvAdapter.setStepItemClick(object : TaskStepViewRvAdapter.TaskStepItemClick {
            override fun OnItem(postion: Int) {
                if (!(taskStepViewRvAdapter.canSelectPosition < postion)) {
                    taskStepViewRvAdapter.selectPosion = postion
//                todaytask_rv.scrollToPosition(postion - 2)
                    linearLayoutManager.scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2, 0)
                    showTime = taskTimeList.get(postion)
                    taskStepViewRvAdapter.notifyDataSetChanged()
                    getTaskList(false)
//                thisTaskList.clear()
//                thisTaskList.addAll(taskList.get(postion)["links"] as ArrayList<MutableMap<String, Any>>)
//                todayTaskAdapter.notifyDataSetChanged()
                }

            }
        })
        intoTime()

    }

}