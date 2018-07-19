package com.overwork.pension.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.overwork.pension.R

/**
 * Created by feima on 2018/7/19.
 */

class TaskStepViewRvAdapter(var context: Context, var timeArrayList: ArrayList<MutableMap<String, Any>>) : RecyclerView.Adapter<TaskStepViewRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stepview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mut = timeArrayList.get(position)
        var type = mut["type"] as Int
        holder.item_stepview_time.setText(mut["time"].toString())
       when(type){
           1->{}
           2->{}
           3->{}
           4->{}
       }
    }

    override fun getItemCount(): Int {
        return timeArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_stepview_left: View
        var item_stepview_time: TextView
        var item_stepview_right: View

        init {
            item_stepview_left = itemView.findViewById(R.id.item_stepview_left)
            item_stepview_time = itemView.findViewById(R.id.item_stepview_time)
            item_stepview_right = itemView.findViewById(R.id.item_stepview_right)
        }
    }
}
