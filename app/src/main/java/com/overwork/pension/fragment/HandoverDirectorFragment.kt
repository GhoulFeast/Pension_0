package com.overwork.pension.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aisino.qrcode.activity.CaptureActivity
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.log
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.QRCODE
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.ClassAdapter
import com.overwork.pension.adapter.HandoverDirectorAdapter
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_class.*
import kotlinx.android.synthetic.main.fragment_handoverdirector.*

/**
 * Created by feima on 2018/7/14.
 */
var isZJ = false

class HandoverDirectorFragment : Fragment() {
    lateinit var classAdapter: HandoverDirectorAdapter
    var classBeans: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_handoverdirector, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
//        getData(arguments.getString("id"))
        if (isZJ) {
            getZG()
        } else {
            getData()
        }

    }

    fun getZG(): Unit {
        Http.post {
            url = BASEURL + T_HANDOVERDIRECTOR
            "userId" - userId

            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")){
                        classBeans.clear()
                        var datas: ArrayList<MutableMap<String, Any>> = ArrayList()
                        datas.addAll("result".."abnormalList")
                        for (map: MutableMap<String, Any> in datas) {
                            if (classBeans.size == 0) {
                                classBeans.add(map)
                            } else {
                                var isHas = false
                                for (classMap: MutableMap<String, Any> in classBeans) {
                                    if (map.get("lrpkid").toString().equals(classMap.get("lrpkid").toString())) {
                                        isHas = true
                                        (classMap.get("informationList") as ArrayList<MutableMap<String, Any>>).addAll(map.get("informationList") as ArrayList<MutableMap<String, Any>>)
                                    }
                                }
                                if (!isHas) {
                                    classBeans.add(map)
                                }
                            }
                        }
                        classAdapter.notifyDataSetChanged()
//                    dialog.dismiss()
                    }else{
                        (!"message").toast(menuActivity)
                    }

                }
            }
            fail {
                menuActivity.runOnUiThread {
                    //                    dialog.dismiss()
                }
            }
        }
    }

    fun getData(): Unit {
        val dialog = LoadingDialog(menuActivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show()
        Http.post {
            url = BASEURL + J_HANDOVERDIRECTOR
            "userId" - menuActivity.getData<String>("jbrid")
            "jbrId" - userId
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        val isJ: Boolean = "result".."isHandove"
                        if (isJ) {
                            Http.post {
                                url = BASEURL + T_HANDOVERDIRECTOR
                                "userId" - userId
                                success {
                                    menuActivity.runOnUiThread {
                                        "交班成功".toast(menuActivity)
                                        classBeans.clear()
                                        var datas: ArrayList<MutableMap<String, Any>> = ArrayList()
                                        datas.addAll("result".."abnormalList")
                                        for (map: MutableMap<String, Any> in datas) {
                                            if (classBeans.size == 0) {
                                                classBeans.add(map)
                                            } else {
                                                var isHas = false
                                                for (classMap: MutableMap<String, Any> in classBeans) {
                                                    if (map.get("lrpkid").toString().equals(classMap.get("lrpkid").toString())) {
                                                        isHas = true
                                                        (classMap.get("informationList") as ArrayList<MutableMap<String, Any>>).addAll(map.get("informationList") as ArrayList<MutableMap<String, Any>>)
                                                    }
                                                }
                                                if (!isHas) {
                                                    classBeans.add(map)
                                                }
                                            }
                                        }
                                        classAdapter.notifyDataSetChanged()
                                        dialog.dismiss()
                                    }
                                }
                                fail {
                                    menuActivity.runOnUiThread {
                                        dialog.dismiss()
                                    }
                                }
                            }

                        }else{
                            dialog.dismiss()
                        }
                    }else{
                        dialog.dismiss()
                    }

                }
            }
            fail {
                menuActivity.runOnUiThread {
                    dialog.dismiss()
                    it.toast(menuActivity)
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == QRCODE) {
                getData()
                var qr = data.getStringExtra("result")
                val code = qr.substring(4, qr.length)
                when (qr.substring(0, 1)) {
                    "Z" -> {
                        if (userType.equals("2")) {
                            isZJ = true
                            (activity as MenuActivity).putData("jbrid", code)
                            getData()
                        }else{
                            "只有主管才能交接班".toast(menuActivity)
                        }
                    }
                    else->{
                        "只能扫描交班二维码，更多功能请使用扫一扫".toast(menuActivity)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun initViewAndEvent(): Unit {
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "交班"
        }
        classAdapter = HandoverDirectorAdapter(classBeans,menuActivity)
        director_rlv.adapter = classAdapter
        director_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                    startActivityForResult(Intent(menuActivity, CaptureActivity::class.java), QRCODE)
            }
        })
    }
}