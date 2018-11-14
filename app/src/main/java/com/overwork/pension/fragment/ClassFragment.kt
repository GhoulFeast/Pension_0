package com.overwork.pension.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aisino.qrcode.encoding.EncodingUtils
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.ClassAdapter
import com.overwork.pension.other.*
import com.overwork.pension.service.IsHandoverService
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_class.*


class ClassFragment : Fragment(), ServiceConnection {

    private var auBinder: IsHandoverService.Binder? = null
    lateinit var classAdapter: ClassAdapter
    var classBeans: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_class, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
        if (userType.toInt() != 2) {
            val intent = Intent(activity, IsHandoverService::class.java)
            activity.bindService(intent, this@ClassFragment, Context.BIND_AUTO_CREATE)
            activity.startService(intent)
        } else {

        }
        getData()
    }

    fun getData(): Unit {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.post {
            url = BASEURL + T_ABNORMAL
            "userId" - userId
            "jblx" - userType
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        classBeans.clear()
                        classBeans.addAll("result".."abnormalList")
                        classAdapter.notifyDataSetChanged()
//                        class_handover_tv.isEnabled=true
                    } else {
                        (!"message").toast(menuActivity)
//                        class_handover_tv.isEnabled=false
                    }
                    dialog.dismiss()
                }
            }
            fail {
                dialog.dismiss()
            }
        }

    }

    fun initViewAndEvent(): Unit {
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "交接班"
        }
        classAdapter = ClassAdapter(classBeans)
        class_rlvv.adapter = classAdapter
//        class_qrcode_ivv.viewTreeObserver.addOnDrawListener({
//            class_qrcode_ivv.setImageBitmap(EncodingUtils.createQRCode("ZY||" + userId, class_qrcode_ivv.width, class_qrcode_ivv.height, null))
//        })
        if (userType.toInt() == 2) {
            class_qrcode_ivv.visibility = View.GONE
            class_handover_tv.visibility=View.VISIBLE
        }else{
            class_handover_tv.visibility=View.GONE
            val h = Handler()
            h.postDelayed({
                class_qrcode_ivv.setImageBitmap(EncodingUtils.createQRCode("ZY||" + userId, class_qrcode_ivv.width, class_qrcode_ivv.height, null))
            }, 300)
        }
        class_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                menuActivity.toDirectorHandover()
            }
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "交接班"
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

    override fun onServiceDisconnected(p0: ComponentName?) {
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        auBinder = p1 as IsHandoverService.Binder
        auBinder?.setRun(true)
        auBinder?.setCallBack(object : IsHandoverService.IsHandoverCall {
            override fun setMsgNum(num: Boolean) {
                menuActivity.runOnUiThread {
                    if (num) {
                        menuActivity.showFragment(HandoverEndFragment())
                        auBinder?.setRun(false)
                        auBinder = null
                    } else {

                    }
                }
            }
        })
    }
}