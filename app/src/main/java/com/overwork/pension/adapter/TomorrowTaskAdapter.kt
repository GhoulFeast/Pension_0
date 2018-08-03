package com.overwork.pension.adapter

import android.content.Context
import android.util.Log
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
class TomorrowTaskAdapter(context: Context, taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {
    var handoverList: ArrayList<MutableMap<String, Any>>
    var mContext: Context

    init {
        mContext = context
        handoverList = taskList
    }

    override fun getView(p0: Int, w: View?, p2: ViewGroup): View {
        Log.i("getView", p0.toString())
        var p1 = LayoutInflater.from(mContext).inflate(R.layout.item_tomorrowtask, null, false)
        var item_tommorrow_name_tv = p1.findViewById<TextView>(R.id.item_tommorrow_name_tv)
        var item_tommorrow_headimage_iv = p1.findViewById<ImageView>(R.id.item_tommorrow_headimage_iv)
        var mutable: MutableMap<String, Any> = handoverList.get(p0)
        Glide.with(mContext).load(mutable["oldPortrait"].toString()).error(R.mipmap.woman).into(item_tommorrow_headimage_iv)
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