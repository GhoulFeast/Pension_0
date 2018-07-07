package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.adapter.TodayTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_today_task.*

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

        return view
    }
}