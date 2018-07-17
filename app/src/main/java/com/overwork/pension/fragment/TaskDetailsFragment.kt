package com.overwork.pension.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.aisino.tool.log
import com.aisino.tool.system.*
import com.aisino.tool.widget.ToastAdd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.overwork.pension.adapter.ProjectAdapter
import com.overwork.pension.adapter.SmallTaskAdapter
import kotlin.collections.ArrayList
import com.bumptech.glide.request.animation.GlideAnimation
import java.io.File


val SOUND = 200

class TaskDetailsFragment : Fragment() {

    lateinit var taskList: MutableMap<String, Any>
    var isDelete = false
    val imageList = mutableMapOf<Int, Bitmap?>()
    val soundList = ArrayList<File>()
    var inageIndex = 0;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_task_details, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MenuActivity).style {
            textBar = ""
        }
        task_details_photograph.setOnClickListener {
            activity.openCameraAndGalleryWindow()
        }
        task_details_sound.setOnClickListener {
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            startActivityForResult(intent, SOUND)
        }
        task_details_save.setOnClickListener {
            Http.get {
                url = BASEURL + ABNORMALITY


            }
            task_details_context.text.toString()
        }
        task_details_record_delete.setOnClickListener {
            ToastAdd.showToast_e(activity, "点击图片或音频删除")
            if (isDelete) {
                isDelete = false
            } else {
                isDelete = true
            }
        }
        intoStepview()
        initList()
//        Glide.with(this).load("http://pic9/258/a2.jpg").into(iv);
//        Glide.with(this).load("file:///xxx.jpg").into(iv);
//        Glide.with(this).load(R.mipmap.ic_launcher).into(iv);
//        Glide.with(this).load(file).into(iv);
//        Glide.with(this).load(uri).into(iv);
//        Glide.with(this).load(byte[]).into(iv);
    }

    fun initList(): Unit {
        Http.get {
            url = BASEURL + THIS_TIME_TASK
            "id" - (activity as MenuActivity).getData<String>(TodayTaskID)
            "userId" - userId
            success {
                val name: String = "result".."name"
                task_details_name.setText(name)
                val sex: String = "result".."sex"
                task_details_sex.setText(sex)
                val romeNo: String = "result".."romeNo"
                task_details_room.setText(romeNo)
                val age: String = "result".."age"
                task_details_age.setText(age)
                taskList = "result".."nursingsAxis"
                task_details_nursing_time.setText(taskList["time"].toString())
                task_details_task.setText(taskList["meal"].toString())
                task_details_task_details.setText(taskList["consideration"].toString())
                task_details_list.adapter = SmallTaskAdapter(activity, taskList["nursings"] as ArrayList<MutableMap<String, Any>>)
                task_details_project_list.adapter = ProjectAdapter(activity, taskList["measurementProject"] as ArrayList<MutableMap<String, Any>>)
                if (taskList["abnormalType"].toString().equals("1")) {
                    task_details_record_needhelp.performClick()
                } else {
                    task_details_record_have.performClick()
                }
                task_details_context.setText(taskList["abnormal"].toString())
                for (img in taskList["imageUrl"] as ArrayList<String>) {
                    val newImg = ImageView(activity)
                    val lp = ViewGroup.LayoutParams(task_details_photograph.width, task_details_photograph.height)
                    newImg.setLayoutParams(lp);
                    newImg.setPadding(18, 0, 0, 0)
                    newImg.left = 24
                    newImg.scaleType = ImageView.ScaleType.CENTER_CROP
                    newImg.setOnClickListener {
                        if (isDelete) {
                            newImg.visibility = View.GONE
                            imageList.remove(it.tag)
                        }
                    }
                    Glide.with(activity).load(img).asBitmap().into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            newImg.setImageBitmap(resource)
                            imageList.put(inageIndex, resource)
                            newImg.tag = inageIndex
                            inageIndex++
                            task_details_picll.addView(newImg)
                        }
                    })
                }

                for (img in taskList["soundUrl"] as ArrayList<String>) {
                    val newImg = ImageView(activity)
                    newImg.setImageResource(R.mipmap.sound_recording)
                    newImg.left = 24
                    newImg.setOnClickListener {
                        if (isDelete) {
                            newImg.visibility = View.GONE

                        }
                        task_details_soull.addView(newImg)
                    }
                }
            }
        }

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

    fun setSimple() {
        task_details_ll_1.visibility = View.GONE
        task_details_ll_2.visibility = View.GONE
        task_details_ll_3.visibility = View.GONE
        task_details_ll_4.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> {
                val newImg = ImageView(activity)
                val bitmap = data?.getParcelableExtra<Uri>(MediaStore.EXTRA_OUTPUT)?.getCameraImg(activity)
                newImg.setImageBitmap(bitmap)
                val lp = ViewGroup.LayoutParams(task_details_photograph.width, task_details_photograph.height)
                newImg.setLayoutParams(lp);
                newImg.setPadding(18, 0, 0, 0)
                newImg.left = 24
                newImg.scaleType = ImageView.ScaleType.CENTER_CROP
                imageList.put(inageIndex, bitmap)
                newImg.tag = inageIndex
                inageIndex++
                newImg.setOnClickListener {
                    if (isDelete) {
                        newImg.visibility = View.GONE
                        imageList.remove(it.tag)
                    }
                }
                task_details_picll.addView(newImg)
            }
            GALLERY_REQUEST -> {
                val newImg = ImageView(activity)
                val bitmap = data?.data?.handleImageOnKitKat(activity)
                newImg.setImageBitmap(bitmap)
                val lp = ViewGroup.LayoutParams(task_details_photograph.width, task_details_photograph.height)
                newImg.setLayoutParams(lp);
                newImg.left = 24
                newImg.scaleType = ImageView.ScaleType.CENTER_CROP
                imageList.put(inageIndex, bitmap)
                newImg.tag = inageIndex
                inageIndex++
                newImg.setOnClickListener {
                    if (isDelete) {
                        newImg.visibility = View.GONE
                        imageList.remove(it.tag)
                    }
                }
                task_details_picll.addView(newImg)
            }
            SOUND -> {
                val newImg = ImageView(activity)
                newImg.setImageResource(R.mipmap.sound_recording)
                newImg.left = 24
                newImg.setOnClickListener {
                    if (isDelete) {
                        newImg.visibility = View.GONE

                    }
                    task_details_soull.addView(newImg)
                }
            }
        }
    }
}



