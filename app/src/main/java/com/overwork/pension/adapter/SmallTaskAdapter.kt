package com.overwork.pension.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.aisino.tool.system.dip2px
import com.aisino.tool.toast
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.fragment.TaskDetailsFragment
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.OVER_TASK
import com.overwork.pension.other.userId
import kotlinx.android.synthetic.main.fragment_task_details.*

class SmallTaskAdapter(val activity: FragmentActivity, val taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(activity).inflate(R.layout.item_small_task, null)
        var task = view.findViewById<TextView>(R.id.item_small_task)
        var complete = view.findViewById<CheckBox>(R.id.item_small_complete)

        task.setText(taskList[p0]["name"].toString())
        isNecessary(view, p0)
        isComplete(view, complete, p0)
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return taskList.size
    }

    fun isNecessary(background: View, p0: Int): Unit {
        if (taskList[p0]["isNecessary"].toString().equals("1")) {//是否必要
            background.setBackgroundColor(activity.resources?.getColor(R.color.redPrimaryLight)!!)
        } else {
            background.setBackgroundColor(activity.resources?.getColor(R.color.white)!!)
        }
    }

    fun isComplete(background: View, complete: CheckBox, p0: Int): Unit {
        complete.setPadding(activity.dip2px(12F), activity.dip2px(5F), activity.dip2px(4F), activity.dip2px(5F))
        if (taskList[p0]["isComplete"].toString().equals("Y")) {//是否完成
//            complete.background = activity.resources?.getDrawable(R.drawable.text_green_raid)
//            complete.setTextColor(activity.resources?.getColor(R.color.white)!!)
            complete.isChecked=true
            background.setOnClickListener {
                "已完成任务".toast(activity)
            }
        } else {
            upTask(background,complete, p0)
        }
    }

    fun upTask(background: View,complete: CheckBox, p0: Int): Unit {//完成任务
//        complete.background = activity.resources?.getDrawable(R.drawable.border_white)
//        complete.setTextColor(activity.resources?.getColor(R.color.mainColor)!!)
//        complete.setPadding(activity.dip2px(16F), activity.dip2px(5F), activity.dip2px(10F), activity.dip2px(5F))
        background.setOnClickListener {
            Http.post {
                url = BASEURL + OVER_TASK
                "userId" - userId
                "zhrwId" - taskList[p0]["zhid"].toString()
                success {
                    menuActivity.runOnUiThread {
                        when (getAny<String>("status")) {
                            "200" -> {
                                "已完成任务".toast(activity)
                                ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
                                complete.isChecked=true
                            }
                            "300" -> {
                                getAny<String>("message").toast(activity)
                                ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
                            }
                            "400" -> {
                                var uploadDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
//                                uploadDialog.setTitle(getAny<String>("message"))
                                val zhnr: String = "result".."zhnr"
                                uploadDialog.setMessage("请完成"+zhnr+"的任务")
                                uploadDialog.setNegativeButton("是", { dialogInterface: DialogInterface, i: Int ->
                                    //                                        val zbpkid: String="result".."zbpkid"//主表id
//                                        val hlrwpkid: String = "result".."hlrwpkid"
                                    ((activity as MenuActivity).showFragment as TaskDetailsFragment).todayTaskID="result".."hlrwpkid"

                                    ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
                                })
                                uploadDialog.setPositiveButton("否", { dialogInterface: DialogInterface, i: Int ->
                                    dialogInterface.dismiss()
                                })
                                uploadDialog.show()
                            }
                        }

                    }
                }
            }
        }
    }
}

