package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

val TodayTaskID = "TodayTaskID"
val lrId = "lrid"
val zbpkId = "zbpkId"

class TodayTaskFragment : Fragment() {
    var time = ""
    var showTime = ""
//    var taskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var taskTimeList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var thisTaskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    lateinit var linearLayoutManager :LinearLayoutManager;

    var pkid=""
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    lateinit var todayTaskAdapter: TodayTaskAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_today_task, container, false)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar=resources.getString(R.string.today_task)
        }
        if (userType.equals("2")){//如果是主管，改变状态为主管添加
            CZLX="03"
        }
        return view
    }

    fun intoTime() {
        var position = 0;
        var thisTime = Calendar.getInstance()
        var minute: Int;
        if (thisTime.get(Calendar.MINUTE) >= 30)
            minute = 30
        else
            minute = 0
        thisTime.set(Calendar.MINUTE, minute)
        var tTime = Calendar.getInstance()
        var i = 0
        tTime.set(Calendar.HOUR_OF_DAY, 6)
        tTime.set(Calendar.MINUTE, 30)
        while (i < 24) {
            tTime.set(Calendar.MINUTE, tTime.get(Calendar.MINUTE) + 30)
            var muMap: MutableMap<String, Any> = mutableMapOf()
            var time = "";
            if (tTime.get(Calendar.MINUTE) == 0) {
                time = "00"
            } else {
                time = tTime.get(Calendar.MINUTE).toString()
            }
            muMap.put("taskTime", tTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + time)
            if (thisTime.get(Calendar.HOUR_OF_DAY) == tTime.get(Calendar.HOUR_OF_DAY) && thisTime.get(Calendar.MINUTE) == tTime.get(Calendar.MINUTE)) {
                position = i;
            }
            taskTimeList.add(muMap)
            i++
        }
        taskStepViewRvAdapter.selectPosion = position
        taskStepViewRvAdapter.notifyDataSetChanged()
//        todaytask_rv.scrollToPosition(position - 2)
        linearLayoutManager.scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2,0)
        showTime = taskTimeList.get(position)["taskTime"].toString()
    }

    fun getTaskList(): Unit {
        Http.post {
            url = BASEURL + T_TASK
            "userId" - userId
            "kssj" - showTime
            if ((activity as MenuActivity).hasData(lrId)){
                "lrpkid" - (activity as MenuActivity).getData<String>(lrId)
//                (activity as MenuActivity).removeData(lrId)
            }
            if((activity as MenuActivity).hasData("cwpkid")){//有cwpkid时加参
                "cwpkid"-(activity as MenuActivity).getData<String>("cwpkid")
//                (activity as MenuActivity).removeData("cwpkid")
            }
            "userType" - userType
            success {
                menuActivity.runOnUiThread {
                if ((!"status").equals("200")){
                    time = "result".."taskTime"
                    thisTaskList.clear()
                    thisTaskList.addAll("result".."links")
                }else{
                    if (todaytask_rv!=null){
                        thisTaskList.clear()
                        taskStepViewRvAdapter.selectPosion = taskStepViewRvAdapter.selectPosion+1
//                        todaytask_rv.scrollToPosition(taskStepViewRvAdapter.selectPosion - 2)
                        linearLayoutManager.scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2,0)
                        showTime = taskTimeList.get(taskStepViewRvAdapter.selectPosion)["taskTime"].toString()
                        taskStepViewRvAdapter.notifyDataSetChanged()
                        if (!showTime.equals("7:00")){
                            getTaskList()
                        }else{
                            "今日已无任务".toast(menuActivity)
                        }


                    }

//                    val posion=taskStepViewRvAdapter.selectPosion
//                    taskStepViewRvAdapter.selectPosion = posion+1
//                    taskStepViewRvAdapter.notifyDataSetChanged()
//                    todaytask_rv.scrollToPosition(posion)
//                    showTime = taskTimeList.get(posion)["taskTime"].toString()
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
                    todayTaskAdapter.showTime=showTime
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
        linearLayoutManager=LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        todaytask_rv.layoutManager =linearLayoutManager
        todaytask_rv.adapter = taskStepViewRvAdapter;
        todaytask_list.adapter = todayTaskAdapter
        todayTaskAdapter.showTime=showTime
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
                taskStepViewRvAdapter.selectPosion = postion
//                todaytask_rv.scrollToPosition(postion - 2)
                linearLayoutManager.scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2,0)
                showTime = taskTimeList.get(postion)["taskTime"].toString()
                taskStepViewRvAdapter.notifyDataSetChanged()
                getTaskList()
//                thisTaskList.clear()
//                thisTaskList.addAll(taskList.get(postion)["links"] as ArrayList<MutableMap<String, Any>>)
//                todayTaskAdapter.notifyDataSetChanged()
            }
        })
        intoTime()
        getTaskList()
    }

}