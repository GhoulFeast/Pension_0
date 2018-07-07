package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.ClassAdapter
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.T_ABNORMAL
import com.overwork.pension.other.T_HANDOVERINFO
import com.overwork.pension.other.userId
import kotlinx.android.synthetic.main.fragment_class.*

/**
 * Created by feima on 2018/7/7.
 */

class HandoverInfoFragment : Fragment() {
    lateinit var handoverInfoAdapter: HandoverInfoAdapter
    var handoverInfos: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_handover, null, false)
        initViewAndEvent()
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        getData()
        super.onViewCreated(view, savedInstanceState)
    }

    fun getData(): Unit {
        Http.get{
            url= BASEURL + T_HANDOVERINFO
            "userId"- userId
            success {
                handoverInfos="result".."handoverList"
                handoverInfoAdapter.notifyDataSetChanged()
            }
        }
    }

    fun initViewAndEvent(): Unit {
        (activity as MenuActivity).setTextView(R.string.checking_information)
        handoverInfoAdapter = HandoverInfoAdapter(handoverInfos)
        class_rlv.adapter=handoverInfoAdapter
        class_handover_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                (activity as MenuActivity).showFragment(HandoverInfoFragment())
            }
        })
        handoverInfoAdapter.setHandover(object :HandoverInfoAdapter.OnHandover{
            override fun OnHandoverClick(id: String) {
                (activity as MenuActivity).showFragment(HandoverInfoFragment())
            }
        })
    }
}
