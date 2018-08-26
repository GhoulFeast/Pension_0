package com.overwork.pension.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
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
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.TaskStepViewRvAdapter
import java.io.File
import java.io.IOException


val SOUND = 200
var CZLX = "01"

class TaskDetailsFragment : Fragment() {


    var taskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var taskStepList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    //    var isDelete = false
    val imageList = ArrayList<FileInfo>()
    val soundList = ArrayList<FileInfo>()
    var abnormal=""
    var abnormalType = "00"
    var todayTaskID=""
    var lrid=""
    var zbpkids=""
    var measurementProjects: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    val RECORD_TYPE_NON = "00"
    val RECORD_TYPE_NEEDHELP = "01"
    val RECORD_TYPE_HAVE = "02"
    var fjxxpkid = ""

    var isSimple = false
    var imgPopupWindow: PopupWindow? = null
    var isDear=false
    lateinit var overDialog: LoadingDialog
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_task_details, null, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overDialog = LoadingDialog(activity).apply { text = "上传中，请稍等" }
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

            when (abnormalType) {
                RECORD_TYPE_NON -> {
                    abnormalType = RECORD_TYPE_NEEDHELP
                    task_details_record_needhelp.isChecked = true
                    task_details_record_ll.visibility = View.VISIBLE
                }
                RECORD_TYPE_NEEDHELP -> {
                    abnormalType = RECORD_TYPE_NON
                    task_details_record_ll.visibility = View.GONE
                    task_details_record_needhelp.isChecked = true
                }
                RECORD_TYPE_HAVE -> {
                    abnormalType = RECORD_TYPE_NEEDHELP
                    task_details_record_needhelp.isChecked = true
                    task_details_record_ll.visibility = View.VISIBLE
                }
            }
            if (isSimple){
                saveAll()

            }
        }
        task_details_record_have.setOnClickListener {
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
            if (isSimple){
                saveAll()
            }
        }
        task_details_context.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                abnormal=p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        lrid=(activity as MenuActivity).getData(lrid)
        (activity as MenuActivity).removeData(lrId)
        todayTaskID=(activity as MenuActivity).getData(TodayTaskID)
        (activity as MenuActivity).removeData(TodayTaskID)
        zbpkids=(activity as MenuActivity).getData(zbpkId)
        (activity as MenuActivity).removeData(zbpkId)
        val han = Handler()
        han.post {
            //延时执行init以等待状态改变
            if (isSimple) {
                task_details_ll_1.visibility=View.GONE
                initSimpleList()
                (activity as MenuActivity).style {
                    textBar = ""
                    titleBar = "异常信息"
                }

            } else {
                initList()
                intoTime()
            }
        }
    }

    fun intoTime() {
        var thisTime = Calendar.getInstance()
        if (!TextUtils.isEmpty(arguments.get("time").toString())) {
            var times = arguments.get("time").toString().split(":")
            thisTime.set(Calendar.MINUTE, times.get(1).toInt())
            thisTime.set(Calendar.HOUR_OF_DAY, times.get(0).toInt())
        }
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

    /**
     * 简单页面加载
     * 19接口
     */
    fun initSimpleList(): Unit {
        Http.post {
            url = BASEURL + SIMPLE_THIS_TIME_TASK
//            if (!CZLX.equals("02")) {
                "hlrwId" - todayTaskID
//            }
            "zbpkid" -zbpkids
            "lrid" - lrid

            "czlx" - CZLX
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        val mut = getAny<ArrayList<MutableMap<String, Any>>>("result")[0]
                        val name: String = mut["name"].toString()
                        task_details_name.setText(name)
                        val sex: String = mut["sex"].toString()
                        task_details_sex.setText(sex)
                        val romeNo: String = mut["romeNo"].toString()
                        task_details_room.setText("房间 " + romeNo)
                        val age: String = mut["age"].toString()
                        task_details_age.setText(age + "周岁")
                        fjxxpkid=mut["fjxxpkid"].toString()
                        when (mut["abnormalType"].toString()) {
                            "01" -> {task_details_record_needhelp.isChecked=true
                                task_details_record_ll.visibility = View.VISIBLE}
                            "02" -> {task_details_record_have.isChecked=true
                                task_details_record_ll.visibility = View.VISIBLE}

                        }

                        task_details_context.setText(mut["abnormal"].toString())
                        task_details_picll.removeAllViews()//重置图片数据
                        imageList.clear()
                        for (img in mut["imageUrl"] as List<MutableMap<String, Any>>) {

                            Glide.with(activity).load(UP_IMAGE + img["wjmc"].toString()).asBitmap().error(R.mipmap.picture).into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                    addImage(null, UP_IMAGE + img["wjmc"].toString(), img["fb1id"].toString()).setImageBitmap(resource)
                                }
                            })
                        }
                        task_details_soull.removeAllViews()
                        soundList.clear()
                        for (sound in mut["soundUrl"] as List<MutableMap<String, Any>>) {
                            addSound(null, UP_SOUND + sound["wjmc"].toString(), sound["fb1id"].toString())
                        }
                    } else {
                        (!"message").toast(activity)
                    }
                }
            }
        }
    }

    fun initList(zbpkid: String = "-1", hlrwpkid: String = "-1"): Unit {
        Http.post {
            url = BASEURL + THIS_TIME_TASK
//            if (!CZLX.equals("02")) {
                "hlrwId" - todayTaskID
//            }
            if (zbpkid.equals("-1")) {//zbpkid未赋值时使用缓存
//                "zbpkid" - (activity as MenuActivity).getData<String>(zbpkId)
            } else {
                "hlrwId" - hlrwpkid
//                "zbpkid" - zbpkid
            }
//            "lrid" - (activity as MenuActivity).getData<String>(lrId)//老人id
            "czlx" - CZLX
            "userId" - userId
//            if (arguments == null) {
//                "time" - ""
//            } else {
//                "time" - arguments.getString("time")
//            }
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        val mut = getAny<ArrayList<MutableMap<String, Any>>>("result")[0]
                        val name: String = mut["name"].toString()
                        task_details_name.setText(name)
                        val sex: String = mut["sex"].toString()
                        task_details_sex.setText(sex)
                        val romeNo: String = mut["romeNo"].toString()
                        task_details_room.setText("房间 " + romeNo)
                        val age: String = mut["age"].toString()
                        task_details_age.setText(age + "周岁")
                        val kssj: String = mut["kssj"].toString()
                        val jssj: String = mut["jssj"].toString()
                        task_details_nursing_time.setText(kssj + " - " + jssj)
                        val meal: String = mut["meal"].toString()
                        task_details_task.setText(meal)
                        val consideration: String = mut["consideration"].toString()
                        fjxxpkid = mut["fjxxpkid"].toString()
                        task_details_task_details.setText(consideration)
                        zbpkids=mut["zbpkid"].toString()
                        taskList.clear()//重置任务数据

                        taskList.addAll(mut["nursings"] as ArrayList<MutableMap<String, Any>>)
                        if (task_details_list.adapter == null) {
                            task_details_list.adapter = SmallTaskAdapter(activity, taskList)
                        } else {
                            (task_details_list.adapter as SmallTaskAdapter).notifyDataSetChanged()
                        }

                        measurementProjects.clear()//重置常规项目数据

                        measurementProjects.addAll(mut["measurementProject"] as ArrayList<MutableMap<String, Any>>)
                        if (task_details_project_list.adapter == null) {
                            task_details_project_list.adapter = ProjectAdapter(activity, measurementProjects)
                        } else {
                            (task_details_project_list.adapter as ProjectAdapter).notifyDataSetChanged()
                        }
                        if (!task_details_record_needhelp.isChecked && !task_details_record_have.isChecked) {
                            when (mut["abnormalType"].toString()) {
                                "01" -> task_details_record_needhelp.performClick()
                                "02" -> task_details_record_have.performClick()
                            }
                        }
                        task_details_context.setText(mut["abnormal"].toString())
                        task_details_picll.removeAllViews()//重置图片数据
                        imageList.clear()
                        for (img in mut["imageUrl"] as List<MutableMap<String, Any>>) {

                            Glide.with(activity).load(UP_IMAGE + img["wjmc"].toString()).asBitmap().error(R.mipmap.picture).into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                    addImage(null, UP_IMAGE + img["wjmc"].toString(), img["fb1id"].toString()).setImageBitmap(resource)
                                }
                            })
                        }
                        task_details_soull.removeAllViews()
                        soundList.clear()
                        for (sound in mut["soundUrl"] as List<MutableMap<String, Any>>) {
                            addSound(null, UP_SOUND + sound["wjmc"].toString(), sound["fb1id"].toString())
                        }
                    } else {
                        (!"message").toast(activity)
                    }

                }
            }

        }

    }


    fun setSimple() {
        val han = Handler()
        han.post {
            task_details_ll_1.visibility = View.GONE
            task_details_ll_2.visibility = View.GONE
            task_details_ll_3.visibility = View.GONE
            task_details_ll_4.visibility = View.GONE
        }
        isSimple = true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> {
                val uri = getCameraUri()
                if (uri?.path != null) {
                    val upImage = File(uri.path)
                    addImage(upImage, "", "").setImageBitmap(uri?.getCameraImg(activity))
                    upLoadImage(upImage, 1)
                } else {
                    "uri==null".log("uuu")
                }
            }
            GALLERY_REQUEST -> {
                val uri = data?.data
                addImage(uri?.toFile(activity), "", "").setImageBitmap(uri?.handleImageOnKitKat(activity))
                upLoadImage(uri?.toFile(activity), 1)
            }
            SOUND -> {
                val uri = data?.data
                addSound(uri, null, "")
                upLoadImage(uri?.toFile(activity), 2)
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
                mediaPlayer.start()
            }
        }
        newImg.setOnLongClickListener {
            removeFile(it as ImageView, soundList.get(it.getTag(R.id.image_id).toString().toInt()), 2)
            "长按删除语音".toast(activity)
            return@setOnLongClickListener true
        }
        newImg.setTag(R.id.image_id, soundList.size)
        if (uri == null) {
            soundList.add(FileInfo(null, soundUrl, id))
        } else {
            soundList.add(FileInfo(uri?.toFile(activity), "", id))
        }
        task_details_soull.addView(newImg)
    }

    fun addImage(file: File?, imageURL: String?, id: String): ImageView {
        val newImg = ImageView(activity)
        val lp = ViewGroup.LayoutParams(task_details_photograph.width + 24, task_details_photograph.height + 24)
        newImg.setLayoutParams(lp);
        newImg.setPadding(24, 24, 24, 24)
        newImg.scaleType = ImageView.ScaleType.CENTER_CROP
        newImg.setTag(R.id.image_id, imageList.size)
        newImg.setOnClickListener {
            imgPopupWindow = (it as ImageView).showFullWindow()
        }
        newImg.setOnLongClickListener {
            removeFile(it as ImageView, imageList.get(it.getTag(R.id.image_id).toString().toInt()), 1)
            "长按删除图片".toast(activity)
            return@setOnLongClickListener true
        }
        task_details_picll.addView(newImg)
        if (file == null) {
            imageList.add(FileInfo(null, imageURL, id))
        } else {
            imageList.add(FileInfo(file, "", id))
        }
        return newImg
    }

    fun removeFile(view: ImageView, string: FileInfo, type: Int) {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show()
        Http.upfile {
            url = BASEURL + DELET_FILE
            "fb1pkid" - string.fileId
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    "删除成功".toast(activity)
                    dialog.dismiss()
                    view.visibility = View.GONE
                    if (type == 1) {
                        imageList.removeAt(view.getTag(R.id.image_id).toString().toInt())
                    } else {
                        soundList.removeAt(view.getTag(R.id.image_id).toString().toInt())
                    }
                }

            }
            fail {
                "删除失败".toast(activity)
                dialog.dismiss()
            }
        }
    }

    fun upLoadImage(path: File?, type: Int): Unit {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.upfile {
            url = BASEURL + UP_FILE
            "file" - path!!
            "zbpkid" - zbpkids
            "fjxxpkid" - fjxxpkid
            "wjlx" - ("0" + type.toString())
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    "上传成功".toast(activity)
                    if (type == 1) {
                        theFileInfoData(path, imageList, "result".."url", "result".."fb1pkid")
                    } else {
                        theFileInfoData(path, soundList, "result".."url", "result".."fb1pkid")
                    }
                    dialog.dismiss()
                }

            }
            fail {
                menuActivity.runOnUiThread {
                    uploadFail(path, type)
                    dialog.dismiss()
                    it.log("fail")
                }
            }
        }
    }

    fun uploadFail(path: File?, type: Int) {
        var uploadDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        uploadDialog.setTitle("上传图片失败")
        uploadDialog.setMessage("是否要重新上传")
        uploadDialog.setNegativeButton("是", { dialogInterface: DialogInterface, i: Int ->
            upLoadImage(path, type)
        })
        uploadDialog.setPositiveButton("否", { dialogInterface: DialogInterface, i: Int ->
            if (type == 1) {
                goneViewTo(task_details_picll, path, imageList);
            } else {
                goneViewTo(task_details_soull, path, soundList);
            }
        })
    }

    fun goneViewTo(view: LinearLayout, page: File?, list: ArrayList<FileInfo>) {
        var i = 0
        while (i < view.childCount) {
            if (view.getChildAt(i).visibility == View.VISIBLE) {
                if (list.get(view.getChildAt(i).getTag(R.id.image_id).toString().toInt()).filePath == page) {
                    view.getChildAt(i).visibility = View.GONE
                    list.removeAt(view.getChildAt(i).getTag(R.id.image_id).toString().toInt())
                }
            }
        }
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
//        var imageString = ""
//        var soundString = ""
//        var measurementString = ""
//        if (imageList.size > 0) {
//            for (image in imageList) {
//                imageString = imageString + "," + image.fileUrl
//            }
//            imageString = imageString.substring(1, imageString.length)
//        }
//
//        if (soundList.size > 0) {
//            for (sound in soundList) {
//                soundString = soundString + "," + sound.fileUrl
//            }
//            soundString = soundString.substring(1, soundString.length)
//        }
//        if (measurementProjects.size > 0) {
//            for (project in measurementProjects) {
//                measurementString = measurementString + "," + project["id"].toString() + "=" + project["num"].toString()
//            }
//            measurementString = measurementString.substring(1, measurementString.length)
//        }

        Http.post {
            url = BASEURL + ABNORMALITY
            "fjxxpkid" - fjxxpkid
            "hlrwId" - todayTaskID
//            (activity as MenuActivity).removeData(TodayTaskID)
            "userId" - userId
            "zbid" - zbpkids
//            (activity as MenuActivity).removeData(zbpkId)
//            "images" - imageString
//            "sounds" - soundString
//            "measurementProject" - measurementString
            "abnormal" - abnormal
            "abnormalType" - abnormalType
            "czlx"- CZLX
            "zbpkid"-zbpkids
            success {
                menuActivity.runOnUiThread {
                    "保存成功".toast(menuActivity)
//                    overDialog.dismiss()
                    if (!isDear){
                        initSimpleList()
                    }
                }

            }
            fail {
                menuActivity.runOnUiThread {
                    "网络错误，保存失败".toast(menuActivity)
//                    overDialog.dismiss()
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            saveAll()
            if (imgPopupWindow != null) {
                imgPopupWindow?.dismiss()
            }

        }
        if (isSimple){
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "异常信息"
            }
        }else{
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "任务详情"
            }
        }

    }

    override fun onDestroy() {
        isDear=true
        saveAll()
        if (imgPopupWindow != null) {
            imgPopupWindow?.dismiss()
        }
//        overDialog.setCanceledOnTouchOutside(false);
//        overDialog.show();
        super.onDestroy()
    }
}