package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.fragment.TaskDetailsFragment
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.OVER_EX
import com.overwork.pension.other.userId
import com.overwork.pension.other.userName


class ProjectAdapter (val activity: FragmentActivity,val taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter(){

    var ifEstimate=0//0实测，1估测

    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_measurement_project, null)
        if (taskList[index]["zt"].toString().equals("1")){
            view.visibility=View.GONE
        }
        val task = view.findViewById<TextView>(R.id.item_project_name)
        val num = view.findViewById<EditText>(R.id.item_project_num)
        when(taskList[index]["lx"].toString()){
            "xy"->{
                task.setText("血压")

            }
            "mb"->{
                task.setText("脉搏")

            }
            "tw"->{
                task.setText("体温")

            }
            "rl"->{
                task.setText("入量")
            }
            "cl"->{
                task.setText("出量")
                val rg = view.findViewById<RadioGroup>(R.id.item_project_rg)
                rg.visibility=View.VISIBLE
                if (taskList[index]["clbz"]!!.equals("0")){
                    rg.check(R.id.item_project_rb0)
                }else{
                    rg.check(R.id.item_project_rb1)
                }
                rg.setOnCheckedChangeListener{ radioGroup: RadioGroup, i: Int ->
                    ifEstimate=i
                }
            }
        }
        num.setText(taskList[index]["sjz"].toString())
        if (!taskList[index]["txr"]!!.equals(userName)&&!taskList[index]["txr"]!!.equals("")){
            num.isEnabled=false
            num.setOnClickListener{
                "该项已被别的用户修改，数据已锁定".toast(activity)
            }
        }else{
            num.setOnFocusChangeListener({ v, hasFocus ->
                Http.get{
                    url=BASEURL+OVER_EX
                    "hlrwId"- taskList[index]["hlrwpkid"].toString()
                    "userId"- userId
                    "lx"-taskList[index]["lx"].toString()
                    "clbz"-ifEstimate.toString()
                    "sjz"- (v as EditText).text.toString()
                    success {
                        activity.runOnUiThread {
                            ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
                        }
                    }
                }
            })
        }
        return view
    }

    override fun getItem(p0: Int): Any {
        return  p0
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getCount(): Int {
        return taskList.size
    }
}