package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity

class HomeFragment :Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)
        (activity as MenuActivity).setTextView(R.string.ylyxt)
        return view

    }




}