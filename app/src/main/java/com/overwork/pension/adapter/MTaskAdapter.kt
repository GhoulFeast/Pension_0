package com.overwork.pension.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class MTaskAdapter :BaseAdapter(){
    private var list: List<MTaskM>? = null
    private var context: Context? = null

    init {
        this.list = list
        this.context = context
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list?.size!!
    }

    class MTaskM{
        var icon:Bitmap?=null
        var name=""
    }
}