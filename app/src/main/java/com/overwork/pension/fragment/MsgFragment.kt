package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.adapter.MsgAdapter
import kotlinx.android.synthetic.main.fragment_msg.*

class MsgFragment :Fragment(){
    private var msgList=ArrayList<MsgAdapter.MsgM>()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_msg, container, false)
        msgList.add(MsgAdapter.MsgM())
        (activity as MenuActivity).setTextView(R.string.wdxx)
        return view
    }

    override fun onStart() {
        super.onStart()
        msg_list.adapter=MsgAdapter(activity,msgList)
    }
}