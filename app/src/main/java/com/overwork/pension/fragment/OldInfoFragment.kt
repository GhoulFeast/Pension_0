package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.log
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.OldInfoAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_oldinfo.*


class OldInfoFragment : Fragment() {
    var oldInfos: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_oldinfo, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "老人信息"
        }
        getData()
        old_info_mlv.isFocusable=false
    }


    fun getData() {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.post {
            url = BASEURL + OLDMAN_INFO
            "userId" - userId
            "lrid" - arguments.getString("id")
            success {
                activity.runOnUiThread {
                    oldInfos = "result".."abnormal"
                    var name: String = "result".."name"
                    old_info_name_tv.setText(name)
                    var sex: String = "result".."sex"
                    old_info_sex_tv.setText(sex)
                    var age: String = "result".."age"
                    old_info_age_tv.setText(age + "周岁")
                    var romeNo: String = "result".."romeNo"
                    old_info_room_tv.setText("房间号" + romeNo)
                    var specials = ArrayList<String>()
                    specials = "result".."special"
                    var specialBuffer = StringBuilder()
                    var i = 0
                    for (str: String in specials) {
                        i++
                        specialBuffer.append(i)
                        specialBuffer.append("、")
                        specialBuffer.append(str)
                        specialBuffer.append("。")
                        specialBuffer.append("\n")
                    }
                    old_info_special.setText(specialBuffer.toString())

                    var name1: String = "result".."name1"
                    var phone1: String = "result".."phone1"
                    var relationship1: String = "result".."relationship1"
                    var emergencyBuffer = StringBuilder()
                    emergencyBuffer.append(relationship1)
                    emergencyBuffer.append("：")
                    emergencyBuffer.append(name1)
                    if (userType.toInt() == 2) {
                        emergencyBuffer.append("   ")
                        emergencyBuffer.append(phone1)
                    }
                    emergencyBuffer.append("\n")
                    var name2: String = "result".."name2"
                    var phone2: String = "result".."phone2"
                    var relationship2: String = "result".."relationship2"
                    emergencyBuffer.append(relationship2)
                    emergencyBuffer.append("：")
                    emergencyBuffer.append(name2)
                    if (userType.toInt() == 2) {
                        emergencyBuffer.append("   ")
                        emergencyBuffer.append(phone2)
                    }
                    old_info_emergency.setText(emergencyBuffer.toString())
                    old_info_mlv.adapter = OldInfoAdapter(activity, oldInfos)
                    dialog.dismiss()
                }
            }

            fail { dialog.dismiss() }
        }
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "老人信息"
        }

    }
}