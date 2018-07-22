package com.overwork.pension.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aisino.qrcode.activity.CaptureActivity
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.ClassAdapter
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_class.*
import kotlinx.android.synthetic.main.fragment_handoverdirector.*

/**
 * Created by feima on 2018/7/14.
 */
class HandoverDirectorFragment : Fragment() {
    lateinit var classAdapter: ClassAdapter
    var classBeans: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_handoverdirector, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
        getData(arguments.getString("id"))
    }

    fun getData(userId: String): Unit {
        Http.get {
            url = BASEURL + T_HANDOVERDIRECTOR
            "userId" - userId
            success {
                activity.runOnUiThread {
                    classBeans.clear()
                    classBeans.addAll("result".."abnormalList")
                    classAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == 1) {
                getData(data.getStringExtra("result"))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun initViewAndEvent(): Unit {
        (activity as MenuActivity).style {
            textBar=activity.resources.getString(R.string.checking_information)
        }
        classAdapter = ClassAdapter(classBeans)
        director_rlv.adapter = classAdapter
        director_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivityForResult(Intent(activity, CaptureActivity::class.java), 1)
            }
        })
    }
}