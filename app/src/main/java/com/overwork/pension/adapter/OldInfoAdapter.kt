package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aisino.tool.widget.ToastAdd
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity

class OldInfoAdapter(val context: FragmentActivity, val list: ArrayList<MutableMap<String, Any>>):BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_oldinfo_abnormal, null)
        var room = view.findViewById<TextView>(R.id.item_old_info_room)
        var times = view.findViewById<TextView>(R.id.item_old_info_times)
        var title = view.findViewById<TextView>(R.id.item_old_info_title)
        var submit_time = view.findViewById<TextView>(R.id.item_old_info_submit_time)
        var submit_name = view.findViewById<TextView>(R.id.item_old_info_submit_name)
        var icontent = view.findViewById<TextView>(R.id.item_old_info_content)
        var image_gv = view.findViewById<GridView>(R.id.item_old_info_image_gv)
        var sound_gv = view.findViewById<GridView>(R.id.item_old_info_sound_gv)

        room.setText(list[p0]["romeNo"].toString())
        times.setText(list[p0]["timeSlot"].toString())
        title.setText(list[p0]["abnormalTitle"].toString() )
        submit_time.setText(list[p0]["submitTime"].toString() )
        submit_name.setText(list[p0]["submitName"].toString() )
        icontent.setText(list[p0]["abnormalContent"].toString() )
        image_gv.adapter=ImageAdapter(context,list[p0]["imageUrl"]!! as ArrayList<String>,0)
        sound_gv.adapter=ImageAdapter(context,list[p0]["soundUrl"]!! as ArrayList<String>,1)
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list?.size!!
    }
}