package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.aisino.tool.toast
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.MSGLIST
import com.overwork.pension.other.MSGLIST_READ
import com.overwork.pension.other.userId
import kotlinx.android.synthetic.main.fragment_msgdetails.*

/**
 * Created by feima on 2018/7/15.
 */
class MsgDetalisFragment : Fragment() {
    var enety: MutableMap<String, Any> = mutableMapOf()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_msgdetails, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        readMsg()
    }

    fun readMsg() {
        Http.post {
            url = BASEURL + MSGLIST_READ
            "userId" - userId
//            "zbpkid" - enety.get("zbpkid").toString()  护理消息的时候有 暂时不用
            "type" - enety.get("type").toString()
            "messageId" - enety.get("messageId").toString()
            success {

            }
        }
    }

    fun setUI() {
        if (enety["type"].toString().equals("0")) {
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "护理消息"
            }
            msg_details_context_ll.visibility = View.VISIBLE
            msg_details_other_context_ll.visibility = View.GONE
            msg_details_handover_tv.visibility = View.VISIBLE
            msg_details_name_tv.setText(enety["name"].toString())
            msg_details_sex_tv.setText(enety.get("sex").toString())
            msg_details_age_tv.setText(enety.get("age").toString() + "周岁")
            msg_details_room_tv.setText("房间号" + enety.get("romeNo").toString())
            msg_details_time_tv.setText(enety.get("kssj").toString() + "-" + enety.get("jssj").toString())
            var tasks = enety.get("tasks").toString().split("||")
            for (map: String in tasks) {
                var taskLl: LinearLayout = LinearLayout(activity)
                taskLl.orientation = LinearLayout.HORIZONTAL
                taskLl.gravity = Gravity.CENTER_VERTICAL
                var view = View(activity)
                view.setBackgroundResource(R.color.mainColor)
                view.layoutParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen.dp_5).toInt(), resources.getDimension(R.dimen.dp_5).toInt())
                var nullView = TextView(activity)
                nullView.setText("\t")
                var taskTv = TextView(activity)
                taskTv.setTextColor(resources.getColor(R.color.text_black))
                taskTv.setText(map)
                taskLl.addView(view)
                taskLl.addView(nullView)
                taskLl.addView(taskTv)
//                taskLl.setPadding(resources.getDimension(R.dimen.dp_2).toInt(), resources.getDimension(R.dimen.dp_5).toInt(),
//                        resources.getDimension(R.dimen.dp_5).toInt(), resources.getDimension(R.dimen.dp_5).toInt())
                msg_details_serious_ll.addView(taskLl)
            }
            msg_details_handover_tv.setOnClickListener({
                var taskDetailsFragment = TaskDetailsFragment();
                var bd = Bundle()
                bd.putString("time", enety.get("kssj").toString())
                taskDetailsFragment.arguments = bd
                (activity as MenuActivity).showFragment(taskDetailsFragment)
                (activity as MenuActivity).putData(TodayTaskID, enety["messageId"].toString())
                (activity as MenuActivity).putData(lrId, enety["id"].toString())
                (activity as MenuActivity).putData(zbpkId, enety["zbpkid"].toString())
            })
        } else {
            msg_details_context_ll.visibility = View.GONE
            msg_details_other_context_ll.visibility = View.VISIBLE
            msg_details_handover_tv.visibility = View.GONE
            msg_details_other_title_tv.setText(enety.get("messageTitle").toString())
            msg_details_other_context_tv.setText(enety.get("messageContent").toString())
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "通知消息"
            }
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (enety["type"].toString().equals("0")) {
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "通知消息"
            }
        } else {
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "护理消息"
            }
        }

    }
}