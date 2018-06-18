package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.overwork.pension.R
import com.overwork.pension.other.UseFragmentManager
import kotlinx.android.synthetic.main.activity_menu.*

class ClassFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_class, null, false)
        initViewAndEvent()
        return view
    }

    fun initViewAndEvent(): Unit {

    }

}