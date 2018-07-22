package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.overwork.pension.R


class ProjectAdapter (activity: FragmentActivity, taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter(){
    private var list: ArrayList<MutableMap<String,Any>>? = null
    private var context: Context? = null

    init {
        this.list = taskList
        this.context = activity
    }

    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_measurement_project, null)
        var task = view.findViewById<TextView>(R.id.item_project_name)
        var num = view.findViewById<TextView>(R.id.item_project_num)

        task.setText(list!![index]["name"].toString())
        num.setText(list!![index]["num"].toString())
        num.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                list!![index].put("num", p0!!)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
        return view
    }

    override fun getItem(p0: Int): Any {
        return  p0
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getCount(): Int {
        return list!!.size
    }
}