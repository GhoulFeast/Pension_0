package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aisino.tool.log
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.fragment.TaskDetailsFragment
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.OVER_EX
import com.overwork.pension.other.userId
import com.overwork.pension.other.userName


class ProjectAdapter(val activity: FragmentActivity, val taskList: ArrayList<MutableMap<String, Any>>) : BaseAdapter() {

    var ifEstimate = 0//0实测，1估测
    val list = ArrayList<ArrayList<MutableMap<String, Any>>>(5)
    var xyd = ""
    var xyg = ""

    init {
        var i = 0
        var twNum = -1
        for (task in taskList) {
            if (task["zt"].toString().equals("1")) {
                list.add(ArrayList<MutableMap<String, Any>>())
                if (task["lx"].toString().equals("tw") || task["lx"].toString().equals("mb")) {
                    if (twNum != -1) {
                        list[twNum].add(task)
                        list.removeAt(i)
                    } else {
                        twNum = i
                        list[i].add(task)
                        i++
                    }
                } else {
                    list[i].add(task)
                    i++
                }
            }
        }
    }

    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_measurement_project, null)
        val task = view.findViewById<TextView>(R.id.item_project_name)
        val num = view.findViewById<EditText>(R.id.item_project_num)
        val task_ll = view.findViewById<LinearLayout>(R.id.project_other_ll)
        val task_other = view.findViewById<TextView>(R.id.item_project_name_other)
        val num_other = view.findViewById<EditText>(R.id.item_project_num_other)

        for (pos in list[index]) {
            when (pos["lx"].toString()) {
                "xy" -> {
                    setXy(task, num, index, task_ll, task_other, num_other)
                }
                "mb" -> {
                    setOther(task, num, index, task_ll, task_other, num_other, "脉搏")
                }
                "tw" -> {
                    setOther(task, num, index, task_ll, task_other, num_other, "体温")
                }
                "rl" -> {
                    task.setText("入量")
                    num.setText(list[index][0]["sjz"].toString())
                    upNum(num, list[index][0]["cgjlpkid"].toString())
                }
                "cl" -> {
                    task.setText("出量")
                    num.setText(list[index][0]["sjz"].toString())
                    val rg = view.findViewById<RadioGroup>(R.id.item_project_rg)
                    rg.visibility = View.VISIBLE
                    if (pos["clbz"]!!.equals("0")) {
                        rg.check(R.id.item_project_rb0)
                    } else {
                        rg.check(R.id.item_project_rb1)
                    }
                    rg.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
                        ifEstimate = i
                    }
                    upNum(num, list[index][0]["cgjlpkid"].toString())
                }
            }
        }


//        if (taskList[index]["zt"].toString().equals("1")) {
//            val task = view.findViewById<TextView>(R.id.item_project_name)
//            val num = view.findViewById<EditText>(R.id.item_project_num)
//
//            num.setText(taskList[index]["sjz"].toString())
//

//        } else {
//            view = LayoutInflater.from(activity).inflate(R.layout.item_nil_view, null)
//            view.visibility = View.GONE
//            view.layoutParams.height=0
//        }

//        num.setOnFocusChangeListener({ v, hasFocus ->
//            if (hasFocus) {
//                // 此处为得到焦点时的处理内容
//            } else {
//                v.id.toString().log("id")
//                // 此处为失去焦点时的处理内容
//                Http.post {
//                    url = BASEURL + OVER_EX
//                    "cgjlpkid" - taskList[index]["hlrwpkid"].toString()
//                    "userId" - userId
//                    "lx" - taskList[index]["lx"].toString()
//                    "clbz" - ifEstimate.toString()
//                    "sjz" - (v as EditText).text.toString()
//                    success {
//                        activity.runOnUiThread {
//                            if (getAny<String>("status").equals("200")) {
//                                ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
//                            } else {
//                                getAny<String>("message").toast(activity)
//                                ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
//                            }
//                        }
//                    }
//                }
//            }
//        })
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    fun setXy(task: TextView, num: EditText, index: Int, task_ll: LinearLayout, task_other: TextView, num_other: EditText): Unit {
        task.setText("低压")
        task_ll.visibility = View.VISIBLE
        task_other.setText("高压")
        val sjz=list[index][0]["sjz"].toString()
        if (sjz.length>0){
            num.setText(sjz.substring(0,sjz.indexOf("~")))
            num_other.setText(sjz.substring(sjz.indexOf("~")+1,sjz.length))
        }
        upNum(num, list[index][0]["cgjlpkid"].toString(), "1")
        upNum(num_other, list[index][0]["cgjlpkid"].toString(), "2")
    }

    fun setOther(task: TextView, num: EditText, index: Int, task_ll: LinearLayout, task_other: TextView, num_other: EditText, t: String): Unit {
        if (task.text.toString().equals("textview")) {
            task.setText(t)
            num.setText(list[index][0]["sjz"].toString())
            upNum(num, list[index][0]["cgjlpkid"].toString())
        } else {
            task_ll.visibility = View.VISIBLE
            task_other.setText(t)
            num_other.setText(list[index][1]["sjz"].toString())
            upNum(num_other, list[index][1]["cgjlpkid"].toString())
        }
    }

    fun upNum(num: EditText, cgjlpkid: String, xyt: String = "0"): Unit {

        num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Http.post {
                    url = BASEURL + OVER_EX
                    "cgjlpkid" - cgjlpkid
                    "userId" - userId
//                    "lx" - taskList[index]["lx"].toString()
                    "clbz" - ifEstimate.toString()
                    when (xyt) {
                        "0" -> "sjz" - p0.toString()
                        "1" -> {
                            "sjz" - (p0.toString() + "~" + xyg)
                            xyd = p0.toString()
                        }
                        "2" -> {
                            "sjz" - (xyd + "~" + p0.toString())
                            xyg = p0.toString()
                        }
                    }
                    success {
                        menuActivity.runOnUiThread {
                            if ((!"status").equals("200")) {
//                                    ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
                            } else {
                                (!"message").toast(activity)
                                ((activity as MenuActivity).showFragment as TaskDetailsFragment).initList()
                            }
                        }
                    }
                }
            }
        })
    }
}


