package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aisino.tool.toast
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
        Http.post {
            url = BASEURL + ROOM_LIST
            "userId" - userId
            if ( (activity as MenuActivity).hasData("fjpkid")){
                "fjpkid"-(activity as MenuActivity).getData<String>("fjpkid")
                (activity as MenuActivity).removeData("fjpkid")
            }
            success {
                activity.runOnUiThread {
                    if ((!"status").equals("200")){
                        roomListBeans.clear()
                        roomListBeans.addAll(getAny<ArrayList<MutableMap<String,Any>>>("result"))
                        roomList.notifyDataSetChanged()
                    }else{
                        (!"message").toast(activity)
                    }

                }
            }
        }

    }

    fun initViewAndEvent(): Unit {
        roomList = RoomListAdapter(roomListBeans)
        roomlist_lv.adapter = roomList
        roomList.setTomorrow(object : RoomListAdapter.OnOld {
            override fun onOldClick(id: String,rwid:String,zbid:String) {
                var toadyTaskFragment = TodayTaskFragment()
                (activity as MenuActivity).showFragment(toadyTaskFragment)
//                (activity as MenuActivity).putData(TodayTaskID, id)
                (activity as MenuActivity).putData(lrId, rwid)
//                (activity as MenuActivity).putData(zbpkId, zbid)
            }
        })
        (activity as MenuActivity).style {
            textBar = ""
            titleBar="房间信息"
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "房间信息"
        }
    }

}