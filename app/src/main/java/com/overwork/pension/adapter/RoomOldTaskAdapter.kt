package com.overwork.pension.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.overwork.pension.R
import com.overwork.pension.other.UP_HEAD
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.lang.Exception

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
        Glide.with(p2.context).load(UP_HEAD+mutable["img"]).error(R.mipmap.woman).bitmapTransform(CropCircleTransformation(p2.context)).into(object : SimpleTarget<GlideDrawable>() {
            override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                item_tommorrow_headimage_iv.setImageDrawable(resource)
            }

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                if (mutable["sex"]!!.equals("男")){
                    item_tommorrow_headimage_iv.setImageResource(R.mipmap.man)
                }else{
                    item_tommorrow_headimage_iv.setImageResource(R.mipmap.woman)
                }
            }
        })
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