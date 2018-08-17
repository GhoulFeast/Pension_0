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
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.T_ABNORMAL
import com.overwork.pension.other.T_HANDOVERINFO
import com.overwork.pension.other.userId
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
        initViewAndEvent()
        getData()
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(activity, IsHandoverService::class.java)
        activity.bindService(intent, this@HandoverInfoFragment, Context.BIND_AUTO_CREATE)
        activity.startService(intent)
    }

    fun getData(): Unit {
        Http.get {
            url = BASEURL + T_HANDOVERINFO
            "userId" - userId
            success {
                activity.runOnUiThread {
                    handoverInfos.clear()
                    handoverInfos.addAll("result".."handoverList")
                    handoverInfoAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun initViewAndEvent(): Unit {
        class_qrcode_iv.viewTreeObserver.addOnDrawListener({
            class_qrcode_iv.setImageBitmap(EncodingUtils.createQRCode("ZY||"+userId, class_qrcode_iv.width, class_qrcode_iv.height, null))
        })
        (activity as MenuActivity).style {
            textBar=activity.resources.getString(R.string.ylyxt)
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
            override fun OnHandoverChangeClick(id: Int, b: Boolean) {

            }
            override fun OnHandoverClick(id: Int) {
                var taslDetalis = TaskDetailsFragment()
                (activity as MenuActivity).putData(TodayTaskID, handoverInfos[id]["oldId"].toString())
                (activity as MenuActivity).putData(lrId, handoverInfos[id]["zbpkid"].toString())
                (activity as MenuActivity).putData(zbpkId, handoverInfos[id]["zbpkid"].toString())
                (activity as MenuActivity).showFragment(taslDetalis)
                taslDetalis.setSimple()
            }
        })
    }

    override fun onServiceDisconnected(p0: ComponentName?) {

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
            override fun setMsgNum(num: String) {
                activity.runOnUiThread {
                    if (num.toBoolean()) {
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
