package com.overwork.pension.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hq.kbase.network.Http
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import java.util.*
import android.provider.MediaStore.Audio.Media.RECORD_SOUND_ACTION
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ImageView
import com.aisino.tool.log
import com.aisino.tool.system.*
import com.aisino.tool.widget.ToastAdd
import com.aisino.tool.widget.showFullWindow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.overwork.pension.adapter.ProjectAdapter
import com.overwork.pension.adapter.SmallTaskAdapter
import kotlin.collections.ArrayList
import com.bumptech.glide.request.animation.GlideAnimation
import com.overwork.pension.adapter.TaskStepViewRvAdapter
import java.io.File
import java.io.IOException


val SOUND = 200

class TaskDetailsFragment : Fragment() {

    var taskList: MutableMap<String, Any> = mutableMapOf()
    var taskStepList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var isDelete = false
    val imageList = ArrayList<File?>()
    val soundList = ArrayList<File?>()
    val imageUpLoadList = ArrayList<File?>()
    val soundUpLoadList = ArrayList<File?>()
    val images = ArrayList<String>()
    val sounds = ArrayList<String>()
    var abnormalType = ""
    lateinit var measurementProjects: ArrayList<MutableMap<String, Any>>
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_task_details, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskStepViewRvAdapter = TaskStepViewRvAdapter(activity, taskStepList)
        todaytask_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        todaytask_rv.adapter = taskStepViewRvAdapter;
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
        task_details_record_needhelp.setOnClickListener {
            abnormalType = "1"
        }
        task_details_record_have.setOnClickListener {
            abnormalType = "2"
        }

        task_details_save.setOnClickListener {
            imageUpLoadList.clear()
            imageUpLoadList.addAll(imageList)
            soundUpLoadList.clear()
            soundUpLoadList.addAll(soundList)
            images.clear()
            sounds.clear()
            if (imageUpLoadList.size > 0) {
                upLoadImage(1)
            } else if (soundUpLoadList.size > 0) {
                upLoadImage(2)
            }
        }
        task_details_record_delete.setOnClickListener {
            ToastAdd.showToast_e(activity, "点击图片或音频删除")
            if (isDelete) {
                isDelete = false
            } else {
                isDelete = true
            }
        }
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
            if (arguments == null) {
                "time" - ""
            } else {
                "time" - arguments.getString("time")
            }
            success {
                activity.runOnUiThread {
                    val name: String = "result".."name"
                    task_details_name.setText(name)
                    val sex: String = "result".."sex"
                    task_details_sex.setText(sex)
                    val romeNo: String = "result".."romeNo"
                    task_details_room.setText("房间 " + romeNo)
                    val age: String = "result".."age"
                    task_details_age.setText(age + "周岁")
                    taskList = "result".."nursingsAxis"
                    taskStepList.addAll("result".."todayTasks")
                    Log.i("1223", taskStepList.size.toString())
                    taskStepViewRvAdapter.notifyDataSetChanged()
                    task_details_nursing_time.setText(taskList["taskTime"].toString())
                    task_details_task.setText(taskList["meal"].toString())
                    task_details_task_details.setText(taskList["consideration"].toString())
                    task_details_list.adapter = SmallTaskAdapter(activity, taskList["nursings"] as ArrayList<MutableMap<String, Any>>)
                    measurementProjects = taskList["measurementProject"] as ArrayList<MutableMap<String, Any>>
                    task_details_project_list.adapter = ProjectAdapter(activity, measurementProjects)
                    if (taskList["abnormalType"].toString().equals("1")) {
                        task_details_record_needhelp.performClick()
                    } else {
                        task_details_record_have.performClick()
                    }
                    task_details_context.setText(taskList["abnormal"].toString())
                    for (img in taskList["imageUrl"] as ArrayList<String>) {
                        Glide.with(activity).load(img).asBitmap().into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                addImage().setImageBitmap(resource)
                            }
                        })
                        images.add(img)
                    }

                    for (sound in taskList["soundUrl"] as ArrayList<String>) {
                        addSound(null, sound)
                    }
                }
            }

        }

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
                val uri = data?.getCameraUri()
                addImage().setImageBitmap(uri?.getCameraImg(activity))
                if (uri?.path != null) {
                    imageList.add(File(uri.path))
                }
            }
            GALLERY_REQUEST -> {
                val uri = data?.data
                addImage().setImageBitmap(uri?.handleImageOnKitKat(activity))
                imageList.add(uri?.toFile(activity))
                imageList[0]?.name?.log()
            }
            SOUND -> {
                addSound(data?.data, null)
            }
        }
    }

    fun addSound(uri: Uri?, soundUrl: String?): Unit {
        val newImg = ImageView(activity)
        newImg.setImageResource(R.mipmap.sound_recording)
        newImg.setPadding(24, 24, 24, 24)
        newImg.setOnClickListener {
            if (isDelete) {
                it.visibility = View.GONE
                soundList.remove(it.tag)
            } else {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.reset()
                if (uri == null) {
                    mediaPlayer.setDataSource(soundUrl)
                } else {
                    mediaPlayer.setDataSource(activity, uri)
                }
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                } else {
                    mediaPlayer.prepare()
                }
            }
        }
        newImg.tag = soundList.size
        if (uri == null) {
            sounds.add(soundUrl!!)
        } else {
            soundList.add(uri?.toFile(activity))
        }
        task_details_soull.addView(newImg)
    }

    fun addImage(): ImageView {
        val newImg = ImageView(activity)
        val lp = ViewGroup.LayoutParams(task_details_photograph.width, task_details_photograph.height)
        newImg.setLayoutParams(lp);
        newImg.setPadding(24, 24, 24, 24)
        newImg.scaleType = ImageView.ScaleType.CENTER_CROP
        newImg.tag = imageList.size
        newImg.setOnClickListener {
            if (isDelete) {
                it.visibility = View.GONE
                imageList.remove(it.tag)
            } else {
                (it as ImageView).showFullWindow()
            }
        }
        task_details_picll.addView(newImg)
        return newImg
    }


    fun upLoadImage(type: Int): Unit {
        Http.upfile {
            url = BASEURL
            if (type == 1) {
                "file" - imageUpLoadList.get(0)!!
            } else if (type == 2) {
                "file" - soundUpLoadList.get(0)!!
            }
            success {
                if (type == 1) {
                    images.add("result".."url")
                    imageUpLoadList.removeAt(0)
                    if (imageUpLoadList.size == 0) {
                        if (soundUpLoadList.size==0){
                            upLoadImage(2)
                        }else{
                            saveAll()
                        }
                    } else {
                        upLoadImage(1)
                    }
                } else if (type == 2) {
                    sounds.add("result".."url")
                    soundUpLoadList.removeAt(0)
                    if (soundUpLoadList.size == 0) {
                        saveAll()
                    } else {
                        upLoadImage(2)
                    }
                }

            }
        }
    }

    fun saveAll(): Unit {
        var imageString = ""
        var soundString = ""
        var measurementString = ""
        if (images.size > 0) {
            for (image in images) {
                imageString = imageString + "," + image
            }
            imageString = imageString.substring(1, imageString.length)
        }

        if (sounds.size > 0) {
            for (sound in sounds) {
                soundString = soundString + "," + sound
            }
            soundString = soundString.substring(1, imageString.length)
        }
        if (measurementProjects.size > 0) {
            for (project in measurementProjects) {
                measurementString = measurementString + "," + project["id"].toString() + "=" + project["num"].toString()
            }
            measurementString = measurementString.substring(1, imageString.length)
        }

        Http.get {
            url = BASEURL + ABNORMALITY
            "id" - (activity as MenuActivity).getData<String>(TodayTaskID)
            "userId" - userId
            "images" - imageString
            "sounds" - soundString
            "measurementProject" - measurementString
            "abnormal" - task_details_context.text.toString()
            "abnormalType" - abnormalType
            success {
                ToastAdd.showToast_r(activity, "保存成功")
            }
        }
    }
}