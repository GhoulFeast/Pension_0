package com.overwork.pension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.overwork.pension.R

/**
 * Created by feima on 2018/7/11.
 */
class TomorrowTaskAdapter(taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {
    var handoverList: List<MutableMap<String, Any>>

    init {
        handoverList = taskList
    }

    override fun getView(p0: Int, p1: View, p2: ViewGroup): View {
        p1 != LayoutInflater.from(p2.context).inflate(R.layout.item_tomorrowtask, p2, false)
        var item_tommorrow_name_tv = p1.findViewById<TextView>(R.id.item_tommorrow_name_tv)
        var item_tommorrow_headimage_iv = p1.findViewById<ImageView>(R.id.item_tommorrow_headimage_iv)
        var mutable: MutableMap<String, Any> = handoverList.get(p0)
        item_tommorrow_name_tv.setText(mutable["oldName"].toString())
        item_tommorrow_headimage_iv.setTag(mutable["oldId"])
        item_tommorrow_headimage_iv.setOnClickListener({ view ->
            var id = view.getTag().toString()
            onTomorrow.OnHandoverClick(id)
        })
        return p1
    }

    lateinit var onTomorrow: OnTomorrow

    fun setTomorrow(onHandov: OnTomorrow) {
        onTomorrow = onHandov
    }

    interface OnTomorrow {
        fun OnHandoverClick(id: String)
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