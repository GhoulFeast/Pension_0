package com.overwork.pension.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baoyachi.stepview.bean.StepBean
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import kotlinx.android.synthetic.main.fragment_task_details.*
import java.util.*

class TaskDetailsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_task_details, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MenuActivity).style {
            textBar = ""
        }
    }

    fun intoStepview() {
        task_details_stepview
        var stepViews: ArrayList<StepBean> = ArrayList<StepBean>()
        System.currentTimeMillis()
        var calendar: Calendar = Calendar.getInstance()
        if (calendar.get(Calendar.MINUTE) >= 30) {
            calendar.set(Calendar.MINUTE, 30)
        } else {
            calendar.set(Calendar.MINUTE, 0)
        }
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 60)
        stepViews.add(StepBean(calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString(), 1))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30)
        stepViews.add(StepBean(calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString(), 1))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 60)
        stepViews.add(StepBean(calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString(), 0))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 90)
        stepViews.add(StepBean(calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString(), -1))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 120)
        stepViews.add(StepBean(calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString(), -1))
        task_details_stepview.setStepViewTexts(stepViews)
                .setTextSize(16)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(activity, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(activity, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(activity, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(activity, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(activity, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(activity, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(activity, R.drawable.attention))//设置StepsViewIndicator AttentionIcon

    }
}