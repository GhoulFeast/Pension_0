package com.overwork.pension.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.overwork.pension.R

/**
 * Created by feima on 2018/7/19.
 */

class TaskStepViewRvAdapter(var context: Context, var timeArrayList: ArrayList<MutableMap<String, Any>>) : RecyclerView.Adapter<TaskStepViewRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        var viewH = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stepview, parent, false))
        viewH.item_stepview_ll.layoutParams.width = (context.resources.displayMetrics.widthPixels - (2 * context.resources.getDimension(R.dimen.dp_10).toInt())) / 5
        viewH.item_stepview_ll.layoutParams.height = (context.resources.displayMetrics.widthPixels) / 5
        return viewH
    }

    var selectPosion = 0;
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mut = timeArrayList.get(position)
        holder.item_stepview_time.setText(mut["taskTime"].toString())
        if (selectPosion == position) {
            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
            holder.item_stepview_time.setBackgroundResource(R.drawable.text_title_blue_raid)
            holder.item_stepview_time.setTextColor(context.resources.getColor(R.color.white))
        } else {
            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
            holder.item_stepview_time.setBackgroundResource(R.drawable.text_title_blue_fffffff_raid)
            holder.item_stepview_time.setTextColor(context.resources.getColor(R.color.title_blue))
        }
        holder.item_stepview_time.setOnClickListener({
            taskStepItemClick?.OnItem(position)
        })
    }

    lateinit var taskStepItemClick: TaskStepItemClick
    fun setStepItemClick(taskStepItem: TaskStepItemClick) {
        taskStepItemClick = taskStepItem
    }

    interface TaskStepItemClick {
        fun OnItem(postion: Int)
    }

    override fun getItemCount(): Int {
        return timeArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_stepview_left: View
        var item_stepview_time: TextView
        var item_stepview_right: View
        var item_stepview_ll: LinearLayout

        init {
            item_stepview_left = itemView.findViewById(R.id.item_stepview_left)
            item_stepview_time = itemView.findViewById(R.id.item_stepview_time)
            item_stepview_right = itemView.findViewById(R.id.item_stepview_right)
            item_stepview_ll = itemView.findViewById(R.id.item_stepview_ll)
        }
    }
}
