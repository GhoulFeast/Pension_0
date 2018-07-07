package com.overwork.pension.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.overwork.pension.R
import kotlinx.android.synthetic.main.item_class_abnormal.*

/**
 * Created by feima on 2018/7/7.
 */

class ClassAdapter(taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {
    val INFORMATIONTYPE_NEEDFOLLOW = 1

    var abnormalList: List<MutableMap<String, Any>>

    init {
        abnormalList = taskList
    }

    override fun getView(p0: Int, p1: View, p2: ViewGroup): View {
        p1 != LayoutInflater.from(p2.context).inflate(R.layout.item_class_abnormal, p2, false)
        var item_class_abnormal_name_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_name_tv)
        var item_class_abnormal_age_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_age_tv)
        var item_class_abnormal_sex_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_sex_tv)
        var item_class_abnormal_room_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_room_tv)
        var item_class_abnormal_needfollow_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_needfollow_ll)
        var item_class_abnormal_serious_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_serious_ll)
        item_class_abnormal_name_tv.setText(abnormalList.get(p0)["name"].toString())
        item_class_abnormal_age_tv.setText(abnormalList.get(p0)["sex"].toString())
        item_class_abnormal_sex_tv.setText(abnormalList.get(p0)["age"].toString())
        item_class_abnormal_room_tv.setText(abnormalList.get(p0)["romeNo"].toString())
        var mutables: List<MutableMap<String, Any>> = abnormalList.get(p0)["informationList"] as List<MutableMap<String, Any>>
        for (map: MutableMap<String, Any> in mutables) {
            var textView = TextView(p2.context)
            textView.setPadding(p2.context.resources.getDimension(R.dimen.dp_5).toInt()
                    , p2.context.resources.getDimension(R.dimen.dp_5).toInt()
                    , p2.context.resources.getDimension(R.dimen.dp_5).toInt()
                    , p2.context.resources.getDimension(R.dimen.dp_5).toInt())
            textView.setText(String.format(p2.context.resources.getString(R.string.needfollow),map["messageContent"].toString()))
            textView.setTextColor(p2.context.resources.getColor(R.color.text_black))
            if (map["type"] as Int == INFORMATIONTYPE_NEEDFOLLOW) {
                item_class_abnormal_needfollow_ll.addView(textView)
            } else {
                item_class_abnormal_serious_ll.addView(textView)
            }
        }
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

