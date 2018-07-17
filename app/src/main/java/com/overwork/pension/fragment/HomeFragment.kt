package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment :Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)
//        (activity as MenuActivity).setTextView(R.string.ylyxt)
        (activity as MenuActivity).style {
            textBar=activity.resources.getString(R.string.ylyxt)
        }
        return view

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_job.setOnClickListener{
            (activity as MenuActivity).showFragment(TodayTaskFragment())
        }
        come_room.setOnClickListener{
            (activity as MenuActivity).showFragment(RoomListFragment())
        }
    }




}