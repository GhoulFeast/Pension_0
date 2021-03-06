package com.overwork.pension.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.overwork.pension.adapter.HandoverDirectorAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_handoverdirector.*

/**
 * Created by feima on 2018/7/14.
 */
var isZJ = false
var iszgjb = false
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
            iszgjb=false
            getData()
        }
        CZLX="03"

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
        val dialog = LoadingDialog(menuActivity)
        dialog.setCanceledOnTouchOutside(false)
        if (!menuActivity.isDestroyed){
            dialog.show()
        }
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
                        (!"message").toast(menuActivity)
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
//                getData()
                var qr = data.getStringExtra("result")
                if (qr.length<4){
                    return
                }
                val code = qr.substring(4, qr.length)
                when (qr.substring(0, 1)) {
                    "Z" -> {
                        if (userType.equals("2")) {
                            isZJ = true
                            menuActivity.putData("jbrid", code)
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
            textBar = "交接班"
        }
        classAdapter = HandoverDirectorAdapter(classBeans,menuActivity)
        director_rlv.adapter = classAdapter
        director_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                    startActivityForResult(Intent(menuActivity, CaptureActivity::class.java), QRCODE)
            }
        })
        director_review_handover_tv.setOnClickListener {
            Http.post {
                url = BASEURL + IS_HANDOVER

                "userId" - userId

                success {
                    menuActivity.runOnUiThread {
                        if ((!"status").equals("200")) {
                            val w: Boolean = "result".."isHandove"
                            if (w) {
                                (activity as MenuActivity).showFragment(HandoverEndFragment())
                            } else {
                                var handoverInfo = HandoverInfoFragment()
                                (activity as MenuActivity).showFragment(handoverInfo)
                            }
                        }else{
                            var handoverInfo = HandoverInfoFragment()
                            (activity as MenuActivity).showFragment(handoverInfo)
                        }
                    }
                }
                fail { }
            }

        }
    }
}