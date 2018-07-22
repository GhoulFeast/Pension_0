package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.OldInfoAdapter
import com.overwork.pension.adapter.TomorrowTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_oldinfo.*
import kotlinx.android.synthetic.main.item_oldinfo_abnormal.*
import kotlinx.android.synthetic.main.item_room.*

class OldInfoFragment : Fragment() {
    var oldInfos: ArrayList<MutableMap<String, Any>> = ArrayList()
    var oldInfo = mutableMapOf<String, Any>()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_oldinfo, container, false)
        (activity as MenuActivity).setTextView(R.string.old_info_title)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    override fun onStart() {
        super.onStart()
    }

    fun getData() {
        Http.get {
            url = BASEURL + OLDMAN_INFO
            "userId" - userId
            success {
                activity.runOnUiThread {
                oldInfo = getAny<MutableMap<String, Any>>("result")
                oldInfos = oldInfo.get("abnormal") as ArrayList<MutableMap<String, Any>>
                setUI()
            }
            }
        }
    }

    fun setUI() {
        old_info_name_tv.setText(oldInfo.get("name").toString())
        old_info_sex_tv.setText(oldInfo.get("sex").toString())
        old_info_age_tv.setText(oldInfo.get("age").toString())
        old_info_room_tv.setText(oldInfo.get("romeNo").toString())
        var specials = oldInfo.get("special") as ArrayList<String>
        var specialBuffer = StringBuilder()
        var i = 0
        for (str: String in specials) {
            i++
            specialBuffer.append(i)
            specialBuffer.append("、")
            specialBuffer.append(str)
            specialBuffer.append("。")
            specialBuffer.append("\\n")
        }
        old_info_special.setText(specialBuffer.toString())
        var emergencys = oldInfo.get("emergency") as ArrayList<MutableMap<String, Any>>
        var emergencyBuffer = StringBuilder()
        for (emergency: MutableMap<String, Any> in emergencys) {
            emergencyBuffer.append(emergency["relationship"])
            emergencyBuffer.append("：")
            emergencyBuffer.append(emergency["name"])
            if (emergency["sex"].toString().toInt()==1){
                emergencyBuffer.append("先生")
            }else{
                emergencyBuffer.append("女士")
            }
            if (userType.toInt()==2) {
                emergencyBuffer.append("   ")
                emergencyBuffer.append(emergency["phone"])
            }
            emergencyBuffer.append("\\n")
        }
        old_info_emergency.setText(emergencyBuffer.toString())
        old_info_mlv.adapter=OldInfoAdapter(activity,oldInfos)
    }
    
}