package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        readMsg();
    }

    fun readMsg() {
        Http.get {
            url = BASEURL + MSGLIST_READ
            "userId" - userId
            success {

            }
        }
    }

    fun setUI() {
        if (enety.get("type") .toString().toInt() == 1) {
            msg_details_context_ll.visibility = View.GONE
            msg_details_other_context_ll.visibility = View.VISIBLE
            msg_details_handover_tv.visibility = View.GONE
            msg_details_other_context_tv.setText(enety.get("messageContent").toString())
            (activity as MenuActivity).setTextView(R.string.msg_title_other)
        } else {
            (activity as MenuActivity).setTextView(R.string.msg_title_task)
            msg_details_context_ll.visibility = View.VISIBLE
            msg_details_other_context_ll.visibility = View.GONE
            msg_details_handover_tv.visibility = View.VISIBLE
            msg_details_name_tv.setText(enety.get("name").toString())
            msg_details_sex_tv.setText(enety.get("sex").toString())
            msg_details_age_tv.setText(enety.get("age").toString() + "周岁")
            msg_details_room_tv.setText(enety.get("romeNo").toString())
            msg_details_time_tv.setText(enety.get("timeSlot ").toString() + "周岁")
            for (map: MutableMap<String, Any> in (enety.get("tasks") as List<MutableMap<String, Any>>)) {
                var taskLl: LinearLayout = LinearLayout(activity)
                taskLl.orientation = LinearLayout.HORIZONTAL
                taskLl.gravity = Gravity.CENTER_VERTICAL
                var view = View(activity)
                view.layoutParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen.dp_2).toInt(), resources.getDimension(R.dimen.dp_2).toInt())
                var taskTv = TextView(activity)
                taskTv.setTextColor(resources.getColor(R.color.text_black))
                taskTv.setText(map.get("taskContent").toString())
                taskLl.addView(view)
                taskLl.addView(taskTv)
                taskLl.setPadding(resources.getDimension(R.dimen.dp_5).toInt(), resources.getDimension(R.dimen.dp_5).toInt(),
                        resources.getDimension(R.dimen.dp_5).toInt(), resources.getDimension(R.dimen.dp_5).toInt())
                msg_details_serious_ll.addView(taskLl)
            }
            msg_details_handover_tv.setOnClickListener({
                (activity as MenuActivity).toHomePage()
            })
        }

    }
}