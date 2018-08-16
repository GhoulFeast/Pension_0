package com.overwork.pension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.overwork.pension.R

/**
 * Created by feima on 2018/7/11.
 */
class RoomOldTaskAdapter(taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {
    var handoverList: List<MutableMap<String, Any>>

    init {
        handoverList = taskList
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        var p1 = LayoutInflater.from(p2.context).inflate(R.layout.item_tomorrowtask, p2, false)
        var item_tommorrow_name_tv = p1.findViewById<TextView>(R.id.item_tommorrow_name_tv)
        var item_tommorrow_headimage_iv = p1.findViewById<ImageView>(R.id.item_tommorrow_headimage_iv)
        var mutable: MutableMap<String, Any> = handoverList.get(p0)
        item_tommorrow_name_tv.setText(mutable["name"].toString())
        Glide.with(p2.context).load(mutable["img"]).error(R.mipmap.woman).into(item_tommorrow_headimage_iv)
        item_tommorrow_headimage_iv.setTag(R.id.room_old_task_id, mutable["lrid"])
        item_tommorrow_headimage_iv.setTag(R.id.room_old_task_rwid, mutable["lrid"])
        item_tommorrow_headimage_iv.setTag(R.id.room_old_task_zbid, mutable["lrid"])
        item_tommorrow_headimage_iv.setOnClickListener({ view ->
            var id = view.getTag(R.id.room_old_task_id).toString()
            var rwid = view.getTag(R.id.room_old_task_rwid).toString()
            var zbid = view.getTag(R.id.room_old_task_zbid).toString()
            onOld.onOldClick(id, rwid, zbid)
        })
        return p1
    }

    lateinit var onOld: OnOld

    fun setTomorrow(onHandov: OnOld) {
        onOld = onHandov
    }

    interface OnOld {
        fun onOldClick(id: String, rwid: String, zbid: String)
    }

    override fun getItem(p0: Int): Any {
        return handoverList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return handoverList.size
    }

}