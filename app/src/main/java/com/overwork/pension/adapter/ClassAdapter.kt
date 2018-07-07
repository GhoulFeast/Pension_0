package com.overwork.pension.adapter


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.overwork.pension.Bean.AbnormalList

/**
 * Created by feima on 2018/7/7.
 */

class ClassAdapter : BaseAdapter() {
    lateinit var abnormalList: List<AbnormalList>
    lateinit var context: Context
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {


        return p1
    }

    override fun getItem(p0: Int): Any {
        return abnormalList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return abnormalList.size
    }



}

