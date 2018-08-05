package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.aisino.tool.ani.LoadingDialog
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.ClassAdapter
import com.overwork.pension.adapter.TodayTaskAdapter
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_class.*
import kotlinx.android.synthetic.main.fragment_today_task.*

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
        Http.get {
            url = BASEURL + T_ABNORMAL
            "userId" - userId
            success {
                activity.runOnUiThread {
                    classBeans.clear()
                    classBeans.addAll("result".."abnormalList")
                    classAdapter.notifyDataSetChanged()
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
            activity.resources.getString(R.string.checking_information)
        }
        classAdapter = ClassAdapter(classBeans)
        class_rlv.adapter = classAdapter
        class_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

            }
        })
    }

}