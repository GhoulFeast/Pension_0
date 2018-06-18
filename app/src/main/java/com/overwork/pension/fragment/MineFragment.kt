package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mine, container, false)
        (activity as MenuActivity).setTextView(R.string.nil)
        return view
    }

    override fun onStart() {
        super.onStart()
        mine_user_name.setText("张三")
    }
}