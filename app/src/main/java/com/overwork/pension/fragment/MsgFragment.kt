package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.MsgAdapter
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.MSGLIST
import com.overwork.pension.other.T_ABNORMAL
import com.overwork.pension.other.userId
import kotlinx.android.synthetic.main.fragment_msg.*

class MsgFragment : Fragment() {
    var msgBeans: ArrayList<MutableMap<String, Any>> = ArrayList()
    lateinit var msgAdapter: MsgAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_msg, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MenuActivity).style {
            textBar = activity.resources.getString(R.string.ylyxt)
        }
        msgAdapter = MsgAdapter(context, msgBeans)
        msg_list.adapter = msgAdapter
        msg_list.setOnItemClickListener({ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            var msgFragment = MsgDetalisFragment();
            msgFragment.enety = msgBeans.get(i)
            (activity as MenuActivity).showFragment(msgFragment)
        })
        getData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = activity.resources.getString(R.string.ylyxt)
        }

        getData()
    }

    fun getData(): Unit {
        Http.post {
            url = BASEURL + MSGLIST
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")){
                        msgBeans.clear()
                        msgBeans.addAll("result".."messageList")
                        msgAdapter.notifyDataSetChanged()
                    }else{
                        (!"message").toast(menuActivity)
                    }

                }
            }
        }

    }
}