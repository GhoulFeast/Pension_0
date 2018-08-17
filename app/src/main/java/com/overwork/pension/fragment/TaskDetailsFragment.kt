package com.overwork.pension.fragment

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.log
import com.aisino.tool.system.*
import com.aisino.tool.toast
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
val CZLX = "01"

class TaskDetailsFragment : Fragment() {

    var taskList: MutableMap<String, Any> = mutableMapOf()
    var taskStepList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    //    var isDelete = false
    val imageList = ArrayList<FileInfo>()
    val soundList = ArrayList<FileInfo>()
    var abnormalType = "00"
    lateinit var measurementProjects: ArrayList<MutableMap<String, Any>>
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    val RECORD_TYPE_NON = "00"
    val RECORD_TYPE_NEEDHELP = "01"
    val RECORD_TYPE_HAVE = "02"
    var fjxxpkid = ""
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
            titleBar = "任务详情"
        }
        task_details_photograph.setOnClickListener {
            activity.openCameraAndGalleryWindow()
        }
        task_details_sound.setOnClickListener {
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            startActivityForResult(intent, SOUND)
        }
//        task_details_save.setOnClickListener {
//            imageUpLoadList.clear()
//            imageUpLoadList.addAll(imageList)
//            soundUpLoadList.clear()
//            soundUpLoadList.addAll(soundList)
//            images.clear()
//            sounds.clear()
//            if (imageUpLoadList.size > 0) {
//                upLoadImage(1)
//            } else if (soundUpLoadList.size > 0) {
//                upLoadImage(2)
//            }
//        }
//        task_details_record_delete.setOnClickListener {
//            if (isDelete) {
//                "点击图片或音频查看或播放".toast(activity)
//                isDelete = false
//            } else {
//                 "点击图片或音频删除".toast(activity)
//                isDelete = true
//            }
//        }
        taskStepViewRvAdapter.setStepItemClick(object : TaskStepViewRvAdapter.TaskStepItemClick {
            override fun OnItem(postion: Int) {
            }
        })
        task_details_record_needhelp.setOnClickListener {
            Log.i("tashelp", "--" + abnormalType)
            when (abnormalType) {
                RECORD_TYPE_NON -> {
                    abnormalType = RECORD_TYPE_NEEDHELP
                    task_details_record_needhelp.isChecked = true
                    task_details_record_ll.visibility = View.VISIBLE
                }
                RECORD_TYPE_NEEDHELP -> {
                    abnormalType = RECORD_TYPE_NON
                    task_details_record_ll.visibility = View.GONE
                    task_details_record_needhelp.isChecked = false
                }
                RECORD_TYPE_HAVE -> {
                    abnormalType = RECORD_TYPE_NEEDHELP
                    task_details_record_needhelp.isChecked = true
                    task_details_record_ll.visibility = View.VISIBLE
                }
            }
            Log.i("tashelp", "-to-" + abnormalType)
        }
        task_details_record_have.setOnClickListener {
            Log.i("tashelp", "--" + abnormalType)
            when (abnormalType) {
                RECORD_TYPE_NON -> {
                    abnormalType = RECORD_TYPE_HAVE
                    task_details_record_ll.visibility = View.VISIBLE
                    task_details_record_have.isChecked = true
                }
                RECORD_TYPE_NEEDHELP -> {
                    task_details_record_have.isChecked = true
                    abnormalType = RECORD_TYPE_HAVE
                    task_details_record_ll.visibility = View.VISIBLE
                }
                RECORD_TYPE_HAVE -> {
                    abnormalType = RECORD_TYPE_NON
                    task_details_record_ll.visibility = View.GONE
                    task_details_record_have.isChecked = false
                }
            }
            Log.i("tashelp", "-to-" + abnormalType)
        }
        intoTime()
        initList()

    }

    fun intoTime() {
        var thisTime = Calendar.getInstance()
        var minute: Int;
        if (thisTime.get(Calendar.MINUTE) >= 30)
            minute = 30
        else
            minute = 0
        thisTime.set(Calendar.MINUTE, minute)
        var time = "";
        thisTime.set(Calendar.MINUTE, thisTime.get(Calendar.MINUTE) - 60)
        var muMap_2: MutableMap<String, Any> = mutableMapOf()
        if (thisTime.get(Calendar.MINUTE) == 0) {
            time = "00"
        } else {
            time = thisTime.get(Calendar.MINUTE).toString()
        }
        muMap_2.put("taskTime", thisTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + time)
        taskStepList.add(muMap_2)
        thisTime.set(Calendar.MINUTE, thisTime.get(Calendar.MINUTE) + 30)
        var muMap_1: MutableMap<String, Any> = mutableMapOf()
        if (thisTime.get(Calendar.MINUTE) == 0) {
            time = "00"
        } else {
            time = thisTime.get(Calendar.MINUTE).toString()
        }
        muMap_1.put("taskTime", thisTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + time)
        taskStepList.add(muMap_1)
        thisTime.set(Calendar.MINUTE, thisTime.get(Calendar.MINUTE) + 30)
        var muMap: MutableMap<String, Any> = mutableMapOf()
        if (thisTime.get(Calendar.MINUTE) == 0) {
            time = "00"
        } else {
            time = thisTime.get(Calendar.MINUTE).toString()
        }
        muMap.put("taskTime", thisTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + time)
        taskStepList.add(muMap)
        thisTime.set(Calendar.MINUTE, thisTime.get(Calendar.MINUTE) + 30)
        var muMapA1: MutableMap<String, Any> = mutableMapOf()
        if (thisTime.get(Calendar.MINUTE) == 0) {
            time = "00"
        } else {
            time = thisTime.get(Calendar.MINUTE).toString()
        }
        muMapA1.put("taskTime", thisTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + time)
        taskStepList.add(muMapA1)
        thisTime.set(Calendar.MINUTE, thisTime.get(Calendar.MINUTE) + 30)
        var muMapA2: MutableMap<String, Any> = mutableMapOf()
        if (thisTime.get(Calendar.MINUTE) == 0) {
            time = "00"
        } else {
            time = thisTime.get(Calendar.MINUTE).toString()
        }
        muMapA2.put("taskTime", thisTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + time)
        taskStepList.add(muMapA2)
        taskStepViewRvAdapter.selectPosion = 2
        taskStepViewRvAdapter.notifyDataSetChanged()
    }

    fun initList(): Unit {
        Http.get {
            url = BASEURL + THIS_TIME_TASK
            if (CZLX.equals("01")) {
                "hlrwId" - (activity as MenuActivity).getData<String>(TodayTaskID)
            }
            "zbpkid" - (activity as MenuActivity).getData<String>(zbpkId)
            "lrid" - (activity as MenuActivity).getData<String>(lrId)
            "czlx" - CZLX
            "userId" - userId
//            if (arguments == null) {
//                "time" - ""
//            } else {
//                "time" - arguments.getString("time")
//            }
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
                    val kssj: String = "result".."kssj"
                    val jssj: String = "result".."Jssj"
                    task_details_nursing_time.setText(kssj + " - " + jssj)
                    val meal: String = "result".."meal"
                    task_details_task.setText(meal)
                    val consideration: String = "result".."consideration"
                    fjxxpkid = "result".."fjxxpkid"
                    task_details_task_details.setText(consideration)
                    taskList = "result".."nursings"
                    if (task_details_list.adapter == null) {
                        task_details_list.adapter = SmallTaskAdapter(activity, taskList["nursings"] as ArrayList<MutableMap<String, Any>>)
                    } else {
                        (task_details_list.adapter as SmallTaskAdapter).notifyDataSetChanged()
                    }
                    measurementProjects = "result".."measurementProject"
                    if (task_details_project_list.adapter == null) {
                        task_details_project_list.adapter = ProjectAdapter(activity, measurementProjects)
                    } else {
                        (task_details_project_list.adapter as ProjectAdapter).notifyDataSetChanged()
                    }
                    when (taskList["abnormalType"].toString()) {
                        "1" -> task_details_record_needhelp.performClick()
                        "2" -> task_details_record_have.performClick()
                    }

                    task_details_context.setText(taskList["abnormal"].toString())
                    task_details_picll.removeAllViews()//重置图片数据
                    imageList.clear()
                    for (img in taskList["imageUrl"] as List<MutableMap<String, Any>>) {

                        Glide.with(activity).load(img).asBitmap().error(R.mipmap.picture).into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                addImage(null, img["wjmc"].toString(), img["fb1id"].toString()).setImageBitmap(resource)
                            }
                        })
                    }
                    task_details_soull.removeAllViews()
                    soundList.clear()
                    for (sound in taskList["soundUrl"] as List<MutableMap<String, Any>>) {
                        addSound(null, sound["wjmc"].toString(), sound["fb1id"].toString())
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
                if (uri?.path != null) {
                    addImage(File(uri.path), "", "").setImageBitmap(uri?.getCameraImg(activity))
                    upLoadImage(File(uri.path), 1)
                }
            }
            GALLERY_REQUEST -> {
                val uri = data?.data
                addImage(uri?.toFile(activity), "", "").setImageBitmap(uri?.handleImageOnKitKat(activity))
                upLoadImage(uri?.toFile(activity), 1)
            }
            SOUND -> {
                addSound(data?.data, null, "")
                upLoadImage(data?.data?.toFile(activity), 1)
            }
        }
    }

    fun addSound(uri: Uri?, soundUrl: String?, id: String): Unit {
        val newImg = ImageView(activity)
        newImg.setImageResource(R.mipmap.sound_recording)
        newImg.setPadding(24, 24, 24, 24)
        newImg.setOnClickListener {
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
        newImg.setOnLongClickListener {
            it.visibility = View.GONE
            removeFile(soundList.get(it.getTag(R.id.image_id).toString().toInt()))
            soundList.remove(it.tag)
            "长按删除语音".toast(activity)
            return@setOnLongClickListener true
        }
        newImg.tag = soundList.size
        if (uri == null) {
            soundList.add(FileInfo(null, soundUrl!!, id))
        } else {
            soundList.add(FileInfo(uri?.toFile(activity), "", id))
        }
        task_details_soull.addView(newImg)
    }

    fun addImage(file: File?, imageURL: String?, id: String): ImageView {
        val newImg = ImageView(activity)
        val lp = ViewGroup.LayoutParams(task_details_photograph.width, task_details_photograph.height)
        newImg.setLayoutParams(lp);
        newImg.setPadding(24, 24, 24, 24)
        newImg.scaleType = ImageView.ScaleType.CENTER_CROP
        newImg.setTag(R.id.image_id, imageList.size)
        newImg.setOnClickListener {
            (it as ImageView).showFullWindow()
        }
        newImg.setOnLongClickListener {
            it.visibility = View.GONE
            removeFile(imageList.get(it.getTag(R.id.image_id).toString().toInt()))
            imageList.remove(it.getTag(R.id.image_id))
            "长按删除图片".toast(activity)
            return@setOnLongClickListener true
        }
        task_details_picll.addView(newImg)
        if (file == null) {
            imageList.add(FileInfo(null, imageURL!!, id))
        } else {
            imageList.add(FileInfo(file, "", id))
        }
        return newImg
    }

    fun removeFile(string: FileInfo) {
        Http.upfile {
            url = BASEURL
            "fb1pkid" - string.fileId
            "userId" - userId
            success {

            }
            fail {

            }
        }
    }

    fun upLoadImage(path: File?, type: Int): Unit {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.upfile {
            url = BASEURL
            "file" - path!!
            "zbpkid" - (activity as MenuActivity).getData<String>(zbpkId)
            "fjxxpkid" - fjxxpkid
            "wjlx" - path.name.substring(path.name.indexOf("."), path.name.length)
            "userId" - userId
            success {
                if (type == 1) {
                    theFileInfoData(path, imageList, "result".."url", "result".."fb1pkid")
                } else {
                    theFileInfoData(path, soundList, "result".."url", "result".."fb1pkid")
                }
                dialog.dismiss()
            }
            fail {
                uploadFail()
                dialog.dismiss()
            }
        }
    }

    fun uploadFail() {
        var uploadDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        uploadDialog.setTitle("上传图片失败")
        uploadDialog.setMessage("是否要重新上传")
        uploadDialog.setNegativeButton("是", { dialogInterface: DialogInterface, i: Int ->

        })
        uploadDialog.setPositiveButton("否", { dialogInterface: DialogInterface, i: Int ->

        })
    }

    fun theFileInfoData(page: File, fileInfos: List<FileInfo>, url: String, id: String): Unit {
        for (fileInfo in fileInfos) {
            if (fileInfo.filePath == page) {
                fileInfo.fileId = id
                fileInfo.fileUrl = url
                return
            }
        }
    }

    fun saveAll(): Unit {
        var imageString = ""
        var soundString = ""
        var measurementString = ""
        if (imageList.size > 0) {
            for (image in imageList) {
                imageString = imageString + "," + image.fileUrl
            }
            imageString = imageString.substring(1, imageString.length)
        }

        if (soundList.size > 0) {
            for (sound in soundList) {
                soundString = soundString + "," + sound.fileUrl
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
            "hlrwId" - (activity as MenuActivity).getData<String>(TodayTaskID)
            "userId" - userId
            "zbid" - (activity as MenuActivity).getData<String>(zbpkId)
            "images" - imageString
            "sounds" - soundString
            "measurementProject" - measurementString
            "abnormal" - task_details_context.text.toString()
            "abnormalType" - abnormalType
            success {
                activity.runOnUiThread {
                    "保存成功".toast(activity)
                    overDialog.dismiss()
                }

            }
            fail {
                activity.runOnUiThread {
                    "网络错误，保存失败".toast(activity)
                    overDialog.dismiss()
                }
            }
        }
    }

    val overDialog = LoadingDialog(activity).apply { text = "上传中，请稍等" }
    override fun onDestroy() {
        saveAll()
        overDialog.setCanceledOnTouchOutside(false);
        overDialog.show();
        super.onDestroy()
    }
}