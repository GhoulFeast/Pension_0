package com.overwork.pension.adapter


import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.overwork.pension.R
import com.overwork.pension.other.userType
import kotlinx.android.synthetic.main.item_class_abnormal.*

/**
 * Created by feima on 2018/7/7.
 */
val INFORMATIONTYPE_NORMAL = "00"
val INFORMATIONTYPE_NEEDFOLLOW = "01"
val INFORMATIONTYPE_SERIOUS = "02"

class ClassAdapter(taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {


    var abnormalList: List<MutableMap<String, Any>>

    init {
        abnormalList = taskList
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        var p1 = LayoutInflater.from(p2.context).inflate(R.layout.item_class_abnormal, p2, false)
        var item_class_abnormal_name_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_name_tv)
        var item_class_abnormal_age_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_age_tv)
        var item_class_abnormal_sex_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_sex_tv)
        var item_class_abnormal_room_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_room_tv)
        var item_class_abnormal_needfollow_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_needfollow_ll)
        var item_class_abnormal_serious_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_serious_ll)
        var item_class_add_abnormal_tv = p1.findViewById<TextView>(R.id.item_class_add_abnormal_tv)
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
        for (map: MutableMap<String, Any> in mutables) {
            var textView = TextView(p2.context)
            textView.setPadding(p2.context.resources.getDimension(R.dimen.dp_5).toInt()
                    , p2.context.resources.getDimension(R.dimen.dp_5).toInt()
                    , p2.context.resources.getDimension(R.dimen.dp_5).toInt()
                    , p2.context.resources.getDimension(R.dimen.dp_5).toInt())
            textView.setText(String.format(p2.context.resources.getString(R.string.needfollow), map["messageContent"].toString()))
            textView.setTextColor(p2.context.resources.getColor(R.color.text_black))
            if (map["type"].toString().equals(INFORMATIONTYPE_NEEDFOLLOW)) {
                item_class_abnormal_needfollow_ll.addView(textView)
            } else if (map["type"].toString().equals(INFORMATIONTYPE_SERIOUS)) {
                item_class_abnormal_serious_ll.addView(textView)
            }
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

