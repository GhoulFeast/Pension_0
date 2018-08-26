package com.overwork.pension.adapter

import android.app.Activity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.fragment.*
import com.overwork.pension.other.userType

/**
 * Created by feima on 2018/7/7.
 */


class HandoverDirectorAdapter(taskList: ArrayList<MutableMap<String, Any>>, activity: Activity) : BaseAdapter() {


    var abnormalList: List<MutableMap<String, Any>>
    var activity: Activity

    init {
        abnormalList = taskList
        this.activity = activity
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        var p1 = LayoutInflater.from(p2.context).inflate(R.layout.item_class_abnormal, p2, false)
        var item_class_abnormal_name_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_name_tv)
        var item_class_abnormal_age_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_age_tv)
        var item_class_abnormal_sex_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_sex_tv)
        var item_class_abnormal_room_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_room_tv)
        var item_class_abnormal_needfollow_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_needfollow_tv)
        var item_class_abnormal_needfollow_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_needfollow_ll_ll)
        var item_class_abnormal_serious_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_serious_tv)
        var item_class_abnormal_serious_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_serious_ll_ll)
        var item_class_add_abnormal_tv = p1.findViewById<TextView>(R.id.item_class_add_abnormal_tv)
        item_class_abnormal_needfollow_tv.setMovementMethod(ScrollingMovementMethod.getInstance())
        item_class_abnormal_serious_tv.setMovementMethod(ScrollingMovementMethod.getInstance())
        item_class_abnormal_name_tv.setText(abnormalList.get(p0)["name"].toString())
        var stringB = StringBuilder();
        stringB.append(abnormalList.get(p0)["age"].toString())
        stringB.append("周岁")
        item_class_abnormal_age_tv.setText(stringB)
        item_class_abnormal_sex_tv.setText(abnormalList.get(p0)["sex"].toString())
        var stringRoom = StringBuilder();
        stringRoom.append("房间号")
        stringRoom.append(abnormalList.get(p0)["romeNo"].toString())
        item_class_abnormal_room_tv.setText(stringRoom)
        var mutables: List<MutableMap<String, Any>> = abnormalList.get(p0)["informationList"] as List<MutableMap<String, Any>>

        var needfollows = ArrayList<String>()
        var seriouss = ArrayList<String>()
        for (map: MutableMap<String, Any> in mutables) {//方法多调用一次
            if (map["type"].toString().equals(INFORMATIONTYPE_NEEDFOLLOW)) {
                var handoverName = "来自" + map.get("handoverName").toString()
                if (needfollows.contains(handoverName)) {
                    needfollows.add(needfollows.indexOf(handoverName), String.format(p2.context.resources.getString(R.string.needfollow), map.get("messageContent").toString()))
                } else {
                    needfollows.add(handoverName)
                }
                needfollows.add(map.get("messageContent").toString())
            } else if (map["type"].toString().equals(INFORMATIONTYPE_SERIOUS)) {
                var handoverName = "来自" + map.get("handoverName").toString()
                if (seriouss.contains(handoverName)) {
                    seriouss.add(seriouss.indexOf(handoverName), String.format(p2.context.resources.getString(R.string.needfollow), map.get("messageContent").toString()))
                } else {
                    seriouss.add(handoverName)
                }
                seriouss.add(map.get("messageContent").toString())
            }
        }
        var needFollowsStringB = StringBuilder()
        for (map: String in needfollows) {
            needFollowsStringB.append(String.format(p2.context.resources.getString(R.string.next_line), map))
        }
        if (needFollowsStringB.length > 0) {
            needFollowsStringB.delete(needFollowsStringB.length - 1, needFollowsStringB.length)
            item_class_abnormal_needfollow_tv.setText(needFollowsStringB.toString())
        }
        var serioussStringB = StringBuilder()
        for (map: String in seriouss) {
            serioussStringB.append(String.format(p2.context.resources.getString(R.string.next_line), map))
        }
        if (serioussStringB.length > 0) {
            serioussStringB.delete(serioussStringB.length - 1, serioussStringB.length)
            item_class_abnormal_serious_tv.setText(serioussStringB.toString())
        }
        if (needfollows.size == 0) {
            item_class_abnormal_needfollow_ll_ll.visibility = View.GONE
        }
        if (seriouss.size == 0) {
            item_class_abnormal_serious_ll_ll.visibility = View.GONE
        }
        if (userType.toInt() == 1) {
            item_class_abnormal_age_tv.setTextColor(p2.context.resources.getColor(R.color.text_black))
            item_class_abnormal_sex_tv.setTextColor(p2.context.resources.getColor(R.color.text_black))
            item_class_abnormal_age_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            item_class_abnormal_sex_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            item_class_add_abnormal_tv.visibility = View.GONE
            item_class_abnormal_room_tv.visibility = View.VISIBLE
        } else {
            item_class_abnormal_age_tv.setTextColor(p2.context.resources.getColor(R.color.mainColor))
            item_class_abnormal_sex_tv.setTextColor(p2.context.resources.getColor(R.color.mainColor))
            item_class_abnormal_age_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            item_class_abnormal_age_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            item_class_add_abnormal_tv.visibility = View.VISIBLE
            item_class_abnormal_room_tv.visibility = View.GONE
        }
        item_class_add_abnormal_tv.setOnClickListener({
            var taskDetailsFragment = TaskDetailsFragment();
            CZLX = "03"
            taskDetailsFragment.setSimple()
            (activity as MenuActivity).showFragment(taskDetailsFragment)
            (activity as MenuActivity).putData(zbpkId, abnormalList.get(p0)["zbpkid"].toString())
        })
        return p1
    }

    override fun getItem(p0: Int): Any {
        return abnormalList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return abnormalList.size
    }


}