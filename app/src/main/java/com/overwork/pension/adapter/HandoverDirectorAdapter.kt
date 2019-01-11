package com.overwork.pension.adapter

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.fragment.*
import com.overwork.pension.other.*

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
        var p1 = LayoutInflater.from(p2.context).inflate(R.layout.item_class_director, p2, false)
        var item_class_abnormal_name_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_name_tv)
        var item_class_abnormal_age_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_age_tv)
        var item_class_abnormal_sex_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_sex_tv)
        var item_class_abnormal_room_tv = p1.findViewById<TextView>(R.id.item_class_abnormal_room_tv)
        var item_class_abnormal_needfollow_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_needfollow_ll)
        var item_class_abnormal_needfollow_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_needfollow_ll_ll)
        var item_class_abnormal_serious_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_serious_ll)
        var item_class_abnormal_serious_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_serious_ll_ll)
        var item_class_abnormal_nothing_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_nothing_ll)
        var item_class_abnormal_nothing_ll_ll = p1.findViewById<LinearLayout>(R.id.item_class_abnormal_nothing_ll_ll)
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
        for (map: MutableMap<String, Any> in mutables) {//方法多调用一次
//            map["messageContent"] = "来自吴佳佳：\\n\\n来自吴佳佳：\\nadssajdhaskdhksaasdasdddddddddddddddddddddddddddddhdkahskdahd"
            var str = map["messageContent"].toString().split("\n")
            var lin = LinearLayout(activity)
            lin.orientation = LinearLayout.VERTICAL
            for (content: String in str) {
                if (content.contains("来自") && content.contains("：")) {
                    var text = TextView(activity)
                    text.setText(content)
                    text.setTextColor(activity.resources.getColor(R.color.title_blue))
                    lin.addView(text)
                } else {
                    var editext = EditText(activity)
                    editext.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    editext.setText(content)
                    editext.setPadding(0, 0, 0, 0)
                    editext.setBackgroundColor(activity.resources.getColor(R.color.page_bg))
                    editext.setTextColor(activity.resources.getColor(R.color.text_black))
                    editext.setTextSize(14f)
                    editext.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {
                        }
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            Http.post {
                                url = BASEURL + Z_UPDATE_ABNORMAL
                                "zgjbpkid" - map["zgjbpkid"].toString()
                                "userId" - userId
                                var stb = StringBuffer()
                                var i = 0;
                                while (i < lin.childCount) {
                                    if (lin.getChildAt(i) is EditText) {
                                        stb.append((lin.getChildAt(i) as EditText).text.toString())
                                    } else if (lin.getChildAt(i) is TextView) {
                                        stb.append((lin.getChildAt(i) as TextView).text.toString())
                                    }
                                    stb.append("\n")
                                    i++
                                }
                                if (stb.length != 0) {
                                    stb.delete(stb.length - 1, stb.length)
                                    "ycnr" - stb.toString()
                                }
                                success {

                                }
                            }
                        }
                    })
                    lin.addView(editext)
                }

            }
            if (map["type"].toString().equals(INFORMATIONTYPE_NEEDFOLLOW)) {
                item_class_abnormal_needfollow_ll.addView(lin)
            } else if (map["type"].toString().equals(INFORMATIONTYPE_SERIOUS)) {
                item_class_abnormal_serious_ll.addView(lin)
            }else if (map["type"].toString().equals(INFORMATIONTYPE_NORMAL)) {
                item_class_abnormal_nothing_ll.addView(lin)
            }
        }

        if (item_class_abnormal_needfollow_ll.childCount == 0) {
            item_class_abnormal_needfollow_ll_ll.visibility = View.GONE
        }
        if (item_class_abnormal_serious_ll.childCount == 0) {
            item_class_abnormal_serious_ll_ll.visibility = View.GONE
        }
        if (item_class_abnormal_nothing_ll.childCount == 0) {
            item_class_abnormal_nothing_ll_ll.visibility = View.GONE
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
            item_class_abnormal_age_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            item_class_abnormal_sex_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            item_class_add_abnormal_tv.visibility = View.VISIBLE
            item_class_abnormal_room_tv.visibility = View.VISIBLE
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