package com.overwork.pension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.overwork.pension.R
import com.overwork.pension.other.MyGridView

/**
 * Created by feima on 2018/7/7.
 */

class RoomListAdapter(taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {

    var links: List<MutableMap<String, Any>>

    init {
        links = taskList
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        var p1 = LayoutInflater.from(p2.context).inflate(R.layout.item_room, p2, false)
        var item_room_tv = p1.findViewById<TextView>(R.id.item_room_tv)
        var item_room_old_mgv = p1.findViewById<MyGridView>(R.id.item_room_old_mgv)
        var roomOldAdapter = RoomOldTaskAdapter(links.get(p0)["people"] as ArrayList<MutableMap<String, Any>>)
        item_room_tv.setText(links.get(p0)["wardNumber"].toString()+"房间")
        item_room_old_mgv.adapter = roomOldAdapter
        roomOldAdapter.setTomorrow(object : RoomOldTaskAdapter.OnOld {
            override fun onOldClick(id: String,rwid:String,zbid:String) {
                onOld.onOldClick(id,rwid,zbid)
            }
        })
        return p1
    }
    lateinit var onOld: OnOld

    fun setTomorrow(onHandov: OnOld) {
        onOld = onHandov
    }

    interface OnOld {
        fun onOldClick(id: String,rwid:String,zbid:String)
    }

    override fun getItem(p0: Int): Any {
        return links.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return links.size
    }


}