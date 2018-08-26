package com.overwork.pension.fragment

import android.os.Bundle
import android.os.Handler
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
import kotlinx.android.synthetic.main.fragment_class.*
import kotlinx.android.synthetic.main.fragment_handover.*


class ClassFragment : Fragment() {
    lateinit var classAdapter: ClassAdapter
    var classBeans: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_class, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
        getData()
    }

    fun getData(): Unit {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.post {
            url = BASEURL + T_ABNORMAL
            "userId" - userId
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
        val h = Handler()
        h.postDelayed({
            class_qrcode_ivv.setImageBitmap(EncodingUtils.createQRCode("ZY||" + userId, class_qrcode_ivv.width, class_qrcode_ivv.height, null))
        }, 300)
//        class_handover_tv.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//
//            }
//        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = ""
            titleBar = "交接班"
        }
    }

}