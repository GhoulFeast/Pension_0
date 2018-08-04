package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.ClassAdapter
import com.overwork.pension.adapter.RoomListAdapter
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.ROOM_LIST
import com.overwork.pension.other.T_ABNORMAL
import com.overwork.pension.other.userId
import kotlinx.android.synthetic.main.fragment_class.*
import kotlinx.android.synthetic.main.fragment_room.*

class RoomListFragment : Fragment() {
    lateinit var roomList: RoomListAdapter
    var roomListBeans: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_room, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
        getData()
    }

    fun getData(): Unit {
        Http.get {
            url = BASEURL + ROOM_LIST
            "userId" - userId
            success {
                activity.runOnUiThread {
                    roomListBeans.clear()
                    roomListBeans.addAll("result".."links")
                    roomList.notifyDataSetChanged()
                }
            }
        }

    }

    fun initViewAndEvent(): Unit {
        roomList = RoomListAdapter(roomListBeans)
        roomlist_lv.adapter = roomList
        roomList.setTomorrow(object : RoomListAdapter.OnOld {
            override fun onOldClick(id: String) {
                var taskDetailsFragment = TaskDetailsFragment();
                (activity as MenuActivity).showFragment(taskDetailsFragment)
                (activity as MenuActivity).putData(TodayTaskID, id)
            }
        })
        (activity as MenuActivity).style {
            textBar = ""
            titleBar="房间信息"
        }
    }

}