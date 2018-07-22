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
        var type = mut["taskState"].toString().toInt()
        holder.item_stepview_time.setText(mut["taskTime"].toString())
        when (type) {
            1 -> {
                holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                holder.item_stepview_time.setBackgroundResource(R.drawable.text_title_blue_raid)
                holder.item_stepview_time.setTextColor(context.resources.getColor(R.color.white))
            }
            2 -> {
                holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                holder.item_stepview_time.setBackgroundResource(R.drawable.text_title_blue_fffffff_raid)
                holder.item_stepview_time.setTextColor(context.resources.getColor(R.color.title_blue))
            }
            3 -> {
                holder.item_stepview_time.setBackgroundResource(R.drawable.text_f88e0c_fffffff_raid)
                holder.item_stepview_time.setTextColor(context.resources.getColor(R.color.color_f88e0c))
                if (position == 0) {
                    holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                } else if (position == timeArrayList.size - 1) {
                    holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                } else {
                    var mutPrevious = timeArrayList.get(position - 1)
                    var typePrevious = mutPrevious["taskState"] .toString().toInt()
                    when (typePrevious) {
                        1 -> {
                            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                        }
                        2 -> {
                            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                        }
                        4 -> {
                            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                        }
                    }
                    var mutNext = timeArrayList.get(position + 1)
                    var typeNext = mutNext["taskState"] .toString().toInt()
                    when (typeNext) {
                        1 -> {
                            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                        }
                        2 -> {
                            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                        }
                        4 -> {
                            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                        }
                    }
                }
            }
            4 -> {
                holder.item_stepview_time.setBackgroundResource(R.drawable.text_title_blue_fffffff_raid)
                holder.item_stepview_time.setTextColor(context.resources.getColor(R.color.title_blue))
                if (position == 0) {
                    holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                } else if (position == timeArrayList.size - 1) {
                    holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                } else {
                    var mutPrevious = timeArrayList.get(position - 1)
                    var typePrevious = mutPrevious["taskState"] .toString().toInt()
                    when (typePrevious) {
                        1 -> {
                            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                        }
                        2 -> {
                            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                        }
                        3 -> {
                            holder.item_stepview_left.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                        }
                    }
                    var mutNext = timeArrayList.get(position + 1)
                    var typeNext = mutNext["taskState"].toString().toInt()
                    when (typeNext) {
                        1 -> {
                            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                        }
                        2 -> {
                            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.color_8d8d8d))
                        }
                        3 -> {
                            holder.item_stepview_right.setBackgroundColor(context.resources.getColor(R.color.title_blue))
                        }
                    }
                }
            }
        }
        holder.item_stepview_time.setOnClickListener({
            taskStepItemClick.OnItem(position)
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
        init {
            item_stepview_left = itemView.findViewById(R.id.item_stepview_left)
            item_stepview_time = itemView.findViewById(R.id.item_stepview_time)
            item_stepview_right = itemView.findViewById(R.id.item_stepview_right)
        }
    }
}
