package com.overwork.pension.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baoyachi.stepview.bean.StepBean
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import java.util.*
import android.provider.MediaStore.Audio.Media.RECORD_SOUND_ACTION
import android.widget.ImageView
import com.aisino.tool.system.CAMERA_REQUEST
import com.aisino.tool.system.GALLERY_REQUEST
import com.aisino.tool.system.openCameraAndGalleryWindow


val IMAGE=100
val SOUND=200

class TaskDetailsFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_task_details, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MenuActivity).style {
            textBar=""
        }
        task_details_photograph.setOnClickListener{
            activity.openCameraAndGalleryWindow()
        }
        task_details_sound.setOnClickListener{
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            startActivityForResult(intent, SOUND)

        }
        task_details_save.setOnClickListener{
            Http.get{
                url= BASEURL+ABNORMALITY


            }
            task_details_context.text.toString()
        }
        intoStepview()
//        Glide.with(this).load("http://pic9/258/a2.jpg").into(iv);
//        Glide.with(this).load("file:///xxx.jpg").into(iv);
//        Glide.with(this).load(R.mipmap.ic_launcher).into(iv);
//        Glide.with(this).load(file).into(iv);
//        Glide.with(this).load(uri).into(iv);
//        Glide.with(this).load(byte[]).into(iv);
    }

    fun intoStepview() {
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

    fun setSimple(){
        task_details_ll_1.visibility=View.GONE
        task_details_ll_2.visibility=View.GONE
        task_details_ll_3.visibility=View.GONE
        task_details_ll_4.visibility=View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_REQUEST-> {
                val newImg= ImageView(activity)
                newImg.setImageURI(data?.data)
                task_details_picll.addView(newImg)
            }
            GALLERY_REQUEST->{
                val newImg= ImageView(activity)
                newImg.setImageURI(data?.data)
                task_details_picll.addView(newImg)
            }
            SOUND->{

            }
        }
    }
}