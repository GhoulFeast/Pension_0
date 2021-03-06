package com.overwork.pension.adapter


import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aisino.tool.log
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
        var item_class_abnormal_needfollow_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_needfollow_tv)
        var item_class_abnormal_needfollow_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_needfollow_ll_ll)
        var item_class_abnormal_serious_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_serious_tv)
        var item_class_abnormal_serious_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_serious_ll_ll)
        var item_class_abnormal_nothing_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_nothing_ll_ll)
        var item_class_abnormal_nothing_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_nothing_tv)
        var item_class_add_abnormal_tv = p1.findViewById<TextView>(R.id.item_class_add_abnormal_tv)
        var item_in_info=p1.findViewById<TextView>(R.id.item_in_info)
        var item_out_info=p1.findViewById<TextView>(R.id.item_out_info)
        var rl =abnormalList.get(p0)["rlhz"].toString()
        if (rl.equals("0")||rl.equals("")){
            rl="无入量数据"
        }else{
            rl="入量:"+rl
        }
        var cl =abnormalList.get(p0)["clhz"].toString()
        if (cl.equals("0")||cl.equals("")){
            cl="无入量数据"
        }else{
            cl="出量:"+cl
        }
        item_in_info.setText(rl)
        item_out_info.setText(cl)
        item_class_abnormal_needfollow_tv.setMovementMethod(ScrollingMovementMethod.getInstance())
        item_class_abnormal_serious_tv.setMovementMethod(ScrollingMovementMethod.getInstance())
        item_class_abnormal_name_tv.setText(abnormalList.get(p0)["name"].toString())
        item_class_abnormal_name_tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
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
        var nothing = ArrayList<String>()
        for (map: MutableMap<String, Any> in mutables) {//方法多调用一次
            if (map["type"].toString().equals(INFORMATIONTYPE_NEEDFOLLOW)) {
                needfollows.add(String.format(p2.context.resources.getString(R.string.needfollow), map["messageContent"].toString()))
            } else if (map["type"].toString().equals(INFORMATIONTYPE_SERIOUS)) {
                seriouss.add(String.format(p2.context.resources.getString(R.string.have_serious_abnormality11), map["messageContent"].toString()))
            }else if (map["type"].toString().equals(INFORMATIONTYPE_NORMAL)) {
                nothing.add(map["messageContent"].toString())
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
        var nothingStringB = StringBuilder()
        for (map: String in nothing) {
            nothingStringB.append(String.format(p2.context.resources.getString(R.string.next_line), map))
        }
        if (nothingStringB.length > 0) {
            nothingStringB.delete(nothingStringB.length - 1, nothingStringB.length)
            item_class_abnormal_nothing_tv.setText(nothingStringB.toString())
        }
        if (TextUtils.isEmpty(item_class_abnormal_needfollow_tv.text.toString())) {
            item_class_abnormal_needfollow_ll_ll.visibility = View.GONE
        }
        if (TextUtils.isEmpty(item_class_abnormal_serious_tv.text.toString())) {
            item_class_abnormal_serious_ll_ll.visibility = View.GONE
        }
        if (TextUtils.isEmpty(item_class_abnormal_nothing_tv.text.toString())) {
            item_class_abnormal_nothing_ll_ll.visibility = View.GONE
        }
        item_class_abnormal_age_tv.setTextColor(p2.context.resources.getColor(R.color.text_black))
        item_class_abnormal_sex_tv.setTextColor(p2.context.resources.getColor(R.color.text_black))
        item_class_abnormal_age_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        item_class_abnormal_sex_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        item_class_abnormal_room_tv.visibility = View.VISIBLE
        item_class_add_abnormal_tv.visibility = View.GONE
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

