package com.overwork.pension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.overwork.pension.R

/**
 * Created by feima on 2018/7/7.
 */

class HandoverInfoAdapter(taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {
    val OLDTYPE_NORMAL = 1

    var handoverList: List<MutableMap<String, Any>>

    init {
        handoverList = taskList
    }

    override fun getView(p0: Int, view: View?, p2: ViewGroup): View {
        var p1 = LayoutInflater.from(p2.context).inflate(R.layout.item_handover_info, p2, false)
        var item_handover_name_tv = p1.findViewById<TextView>(R.id.item_handover_name_tv)
        var item_handover_complete_iv = p1.findViewById<CheckBox>(R.id.item_handover_complete_iv)
        var item_handover_type_tv = p1.findViewById<TextView>(R.id.item_handover_type_tv)
        var item_handover_additional_iv = p1.findViewById<TextView>(R.id.item_handover_additional_iv)
        var mutable: MutableMap<String, Any> = handoverList.get(p0)
        item_handover_name_tv.setText(mutable["oldName"].toString())
        if (mutable["oldType"].toString().toInt() == OLDTYPE_NORMAL) {
            item_handover_type_tv.setText("正常")
            item_handover_type_tv.setTextColor(p2.context.resources.getColor(R.color.text_black))
        } else {
            item_handover_type_tv.setText("异常")
            item_handover_type_tv.setTextColor(p2.context.resources.getColor(R.color.color_f8120c))
        }
        if (mutable["isRecheck"].toString().toBoolean()) {
            item_handover_complete_iv.isChecked = true
        } else {
            item_handover_complete_iv.isChecked = false
        }
        item_handover_complete_iv.setOnCheckedChangeListener { compoundButton, b ->
            var position = compoundButton.getTag().toString().toInt()
            onHandover.OnHandoverChangeClick(position, b)
        }
        item_handover_additional_iv.setTag(p0)
        item_handover_additional_iv.setOnClickListener({ view ->

            var position = view.getTag().toString().toInt()
            onHandover.OnHandoverClick(position)

        })
        return p1
    }

    lateinit var onHandover: OnHandover

    fun setHandover(onHandov: OnHandover) {
        onHandover = onHandov
    }

    interface OnHandover {
        fun OnHandoverClick(id: Int)
        fun OnHandoverChangeClick(id: Int, b: Boolean)
    }

    override fun getItem(p0: Int): Any {
        return handoverList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return handoverList.size
    }


}