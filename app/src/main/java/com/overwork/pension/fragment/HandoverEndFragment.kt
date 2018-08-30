package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity

/**
 * Created by feima on 2018/7/11.
 */
class HandoverEndFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_handoverend, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MenuActivity).style {
            textBar = "交班完成"
//            titleBar="交班完成"
        }
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = "交班完成"
//            titleBar = "交班完成"
        }
    }
}