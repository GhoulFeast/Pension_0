package com.overwork.pension.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.aisino.qrcode.encoding.EncodingUtils
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.toast
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.other.*
import com.overwork.pension.service.IsHandoverService
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_handover.*
import kotlinx.android.synthetic.main.fragment_handover.view.*

/**
 * Created by feima on 2018/7/7.
 */

class HandoverInfoFragment : Fragment(), ServiceConnection {
    private var auBinder: IsHandoverService.Binder? = null
    lateinit var handoverInfoAdapter: HandoverInfoAdapter
    var handoverInfos: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_handover, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
        getData()
        val intent = Intent(activity, IsHandoverService::class.java)
        activity.bindService(intent, this@HandoverInfoFragment, Context.BIND_AUTO_CREATE)
        activity.startService(intent)
    }

    fun getData(): Unit {
        auBinder?.setRun(true)
        Http.post {
            url = BASEURL + IS_HANDOVER
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        val isJ: Boolean = "result".."isHandove"
                        if (isJ) {
                            (activity as MenuActivity).showFragment(HandoverEndFragment())
                            auBinder?.setRun(false)
                            auBinder = null
                        }
                    }
                }
            }

            fail { }
        }
        Http.post {
            url = BASEURL + T_HANDOVERINFO
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        handoverInfos.clear()
                        handoverInfos.addAll("result".."handoverList")
                        handoverInfoAdapter.notifyDataSetChanged()
                        for (map: MutableMap<String, Any> in handoverInfos) {
                            if (map.get("isRecheck").toString().equals("N")) {
                                class_handover_tv.isEnabled = false
                                class_handover_tv.alpha = 0.3f
                                break
                            }
                        }
                    } else {
                        (!"message").toast(activity)
                        class_handover_tv.isEnabled = false
                        class_handover_tv.alpha = 0.3f
                    }
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = activity.resources.getString(R.string.ylyxt)
        }
    }

    fun initViewAndEvent(): Unit {
        (activity as MenuActivity).style {
            textBar = activity.resources.getString(R.string.ylyxt)
        }
        handoverInfoAdapter = HandoverInfoAdapter(handoverInfos)
        class_rlv.adapter = handoverInfoAdapter
        class_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var classFragment = ClassFragment()
                (activity as MenuActivity).showFragment(classFragment)
            }
        })
        handoverInfoAdapter.setHandover(object : HandoverInfoAdapter.OnHandover {
            override fun OnHandoverChangeClick(position: Int, b: Boolean) {
                submitHandover(handoverInfos[position].get("fcjlid").toString(), position,b)
            }

            override fun OnHandoverClick(id: Int) {
                var taslDetalis = TaskDetailsFragment()
//                var bd = Bundle()
//                bd.putString("time", showTime)
//                taslDetalis.arguments = bd
                CZLX="02"
                (activity as MenuActivity).putData(TodayTaskID, handoverInfos[id]["oldId"].toString())
                (activity as MenuActivity).putData(zbpkId, handoverInfos[id]["zbpkid"].toString())
                (activity as MenuActivity).showFragment(taslDetalis)
                taslDetalis.setSimple()
            }
        })
    }

    override fun onServiceDisconnected(p0: ComponentName?) {

    }

    fun submitHandover(fcjlid: String, position: Int,boolean: Boolean) {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.post {
            url = BASEURL + T_SUBMITHANDOVER
            "userId" - userId
            "fcjlid" - fcjlid
            if (boolean){
                "fczt"-"Y"
            }else{
                "fczt"-"N"
            }

            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        var isHandover: Boolean = "result".."isHandove"
                        if (isHandover) {
                            handoverInfos.get(position)["isRecheck"] = "Y"
                        } else {
                            handoverInfos.get(position)["isRecheck"] = "N"
                        }
                        var isAll = true
                        for (map: MutableMap<String, Any> in handoverInfos) {
                            if (map.get("isRecheck").toString().equals("N")) {
                                isAll = false
                                break
                            }
                        }
                        if (isAll) {
                            class_handover_tv.isEnabled = true
                            class_handover_tv.alpha = 1.0f
                        } else {
                            class_handover_tv.isEnabled = false
                            class_handover_tv.alpha = 0.3f
                        }
                        getData()
                    }

                    dialog.dismiss()
                }
            }
            fail {
                handoverInfoAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        auBinder?.setRun(false)
    }

    override fun onStart() {
        super.onStart()
        auBinder?.setRun(true)

    }

    override fun onDestroy() {
        super.onDestroy()
        auBinder = null
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        auBinder = p1 as IsHandoverService.Binder
        auBinder?.setRun(true)
        auBinder?.setCallBack(object : IsHandoverService.IsHandoverCall {
            override fun setMsgNum(num: Boolean) {
                menuActivity.runOnUiThread {
                    if (num) {
                        (activity as MenuActivity).showFragment(HandoverEndFragment())
                        auBinder?.setRun(false)
                        auBinder = null
                    } else {

                    }
                }
            }
        })
    }

}
