package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.adapter.TomorrowTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_today_task.*

class MineFragment : Fragment() {
    lateinit var tomorrowTaskAdp: TomorrowTaskAdapter
    var tomorrowTasks: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mine, container, false)
        (activity as MenuActivity).setTextView(R.string.nil)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tomorrowTaskAdp = TomorrowTaskAdapter(tomorrowTasks)
        mine_task_list.adapter = tomorrowTaskAdp
        mine_user_name.setText(userName)
        mine_user_job.setText(userPortrait)
        mine_user_starttime.setText(String.format(resources.getString(R.string.text_entryTime, entryTime)))
        mine_user_overtime.setText(String.format(resources.getString(R.string.text_entryTime, workingYears)))
        mine_user_ld.setText(String.format(resources.getString(R.string.text_entryTime, superiorName)))
        mine_exit_user.setOnClickListener { }
        mine_task_list.setOnItemClickListener({ adapterView, view, position, long ->

        })
        getData()
    }

    override fun onStart() {
        super.onStart()
        mine_user_name.setText("张三")
    }

    fun getData() {
        Http.get {
            url = BASEURL + TOMORROW_TASK
            "userId" - userId
            success {
                tomorrowTasks = "result".."result"
                tomorrowTaskAdp.notifyDataSetChanged()
            }
        }
    }
}