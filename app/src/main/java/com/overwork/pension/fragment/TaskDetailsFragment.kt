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
import java.io.*


val SOUND = 200
var CZLX = "01"

class TaskDetailsFragment : Fragment() {


    var taskList: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    var taskStepList: ArrayList<String> = ArrayList<String>()
    //    var isDelete = false
    val imageList = ArrayList<FileInfo>()
    val soundList = ArrayList<FileInfo>()
    var abnormal = ""
    var abnormalType = "00"
    var todayTaskID = ""
    var lrid = ""
    var zbpkids = ""
    var measurementProjects: ArrayList<MutableMap<String, Any>> = ArrayList<MutableMap<String, Any>>()
    lateinit var taskStepViewRvAdapter: TaskStepViewRvAdapter
    val RECORD_TYPE_NON = "00"
    val RECORD_TYPE_NEEDHELP = "01"
    val RECORD_TYPE_HAVE = "02"
    var fjxxpkid = ""
    var isfjxxpkid = false//附件id的锁

    var isSimple = false
    var imgPopupWindow: PopupWindow? = null
    var isDear = false
    lateinit var overDialog: LoadingDialog
    var isOnce = true//是否第一次调研异常文本
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
            menuActivity.openCameraAndGalleryWindow()
        }
        task_details_sound.setOnClickListener {
            try {
                val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                startActivityForResult(intent, SOUND)
            } catch (e: Exception) {
                "无法使用录音功能".toast(menuActivity)
            }

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
            if (isfjxxpkid) {
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
            if (isfjxxpkid) {
                saveAll()
            }

        }
        task_details_context.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                abnormal = p0.toString()
                if (isOnce) {
                    isOnce = false
                } else {
                    saveAll()
                }
            }

        })

        lrid = (activity as MenuActivity).getData(lrid)
        (activity as MenuActivity).removeData(lrId)
        todayTaskID = (activity as MenuActivity).getData(TodayTaskID)
        (activity as MenuActivity).removeData(TodayTaskID)
        zbpkids = (activity as MenuActivity).getData(zbpkId)
        (activity as MenuActivity).removeData(zbpkId)
        val han = Handler()
        han.post {
            //延时执行init以等待状态改变
            if (isSimple) {
                task_details_ll_1.visibility = View.GONE
                initSimpleList()
                (activity as MenuActivity).style {
                    textBar = ""
                    titleBar = "异常信息"
                }

            } else {
                initList()
            }
        }
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
            "zbpkid" - zbpkids
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
                        fjxxpkid = mut["fjxxpkid"].toString()
                        when (mut["abnormalType"].toString()) {
                            "01" -> {
                                abnormalType = "01"
                                task_details_record_needhelp.isChecked = true
                                task_details_record_ll.visibility = View.VISIBLE
                            }
                            "02" -> {
                                abnormalType = "02"
                                task_details_record_have.isChecked = true
                                task_details_record_ll.visibility = View.VISIBLE
                            }

                        }

                        task_details_context.setText(mut["abnormal"].toString())
                        abnormal = mut["abnormal"].toString()
                        task_details_picll.removeAllViews()//重置图片数据
                        imageList.clear()
                        for (img in mut["imageUrl"] as List<MutableMap<String, Any>>) {
                            addImage(null, UP_IMAGE + img["wjmc"].toString(), img["fb1id"].toString())
                        }
                        task_details_soull.removeAllViews()
                        soundList.clear()
                        for (sound in mut["soundUrl"] as List<MutableMap<String, Any>>) {
                            addSound(null, UP_SOUND + sound["wjmc"].toString(), sound["fb1id"].toString())
                        }
                    } else {
                        (!"message").toast(menuActivity)
                    }
                    isfjxxpkid = true
                }
            }
        }
    }

    fun initList(): Unit {
        Http.post {
            url = BASEURL + THIS_TIME_TASK
//            if (!CZLX.equals("02")) {
            "hlrwId" - todayTaskID
//            }
//            "zbpkid"-zbpkids
//            "lrid"-lrid
//            if (zbpkid.equals("-1")) {//zbpkid未赋值时使用缓存
//                "zbpkid" - (activity as MenuActivity).getData<String>(zbpkId)
//            } else {
//                "hlrwId" - hlrwpkid
//                "zbpkid" - zbpkid
//            }
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
                        taskStepList.clear()
                        for (map in mut["timeaxis"] as ArrayList<MutableMap<String, Any>>) {
                            taskStepList.add(map["kssj"].toString())
                        }
                        selecttime = kssj
                        taskStepViewRvAdapter.selectPosion = taskStepList.indexOf(kssj)
                        taskStepViewRvAdapter.notifyDataSetChanged()
                        todaytask_rv.post {
                            if (taskStepViewRvAdapter.selectPosion - 2 < 0) {
                                (todaytask_rv.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                            } else {
                                (todaytask_rv.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(taskStepViewRvAdapter.selectPosion - 2, 0)
                            }
                        }
                        val jssj: String = mut["jssj"].toString()
                        task_details_nursing_time.setText(kssj + " - " + jssj)
                        val meal: String = mut["meal"].toString()
                        task_details_task.setText(meal)
                        val consideration: String = mut["consideration"].toString()
                        fjxxpkid = mut["fjxxpkid"].toString()
                        task_details_task_details.setText(consideration)
                        zbpkids = mut["zbpkid"].toString()
                        taskList.clear()//重置任务数据

                        taskList.addAll(mut["nursings"] as ArrayList<MutableMap<String, Any>>)
                        if (task_details_list.adapter == null) {
                            task_details_list.adapter = SmallTaskAdapter(menuActivity, taskList)
                        } else {
                            (task_details_list.adapter as SmallTaskAdapter).notifyDataSetChanged()
                        }
                        var showPro = false//判断常规项目是否可以显示
                        val projects = mut["measurementProject"] as ArrayList<MutableMap<String, Any>>
                        for (project in projects) {
                            if (project["zt"].toString().equals("1")) {
                                showPro = true
                                break
                            }
                        }
                        measurementProjects.clear()//重置常规项目数据
                        if (showPro) {
                            task_details_ll_4.visibility = View.VISIBLE
                            measurementProjects.addAll(mut["measurementProject"] as ArrayList<MutableMap<String, Any>>)
                            if (task_details_project_list.adapter == null) {
                                task_details_project_list.adapter = ProjectAdapter(menuActivity, measurementProjects)
                            } else {
                                (task_details_project_list.adapter as ProjectAdapter).notifyDataSetChanged()
                            }
                        } else {
                            task_details_ll_4.visibility = View.GONE
                        }
                        task_details_context.setText(mut["abnormal"].toString())
                        abnormal = mut["abnormal"].toString()

                        if (!task_details_record_needhelp.isChecked && !task_details_record_have.isChecked) {
                            when (mut["abnormalType"].toString()) {
                                "01" -> {
                                    abnormalType = "01"
                                    task_details_record_needhelp.isChecked = true
                                    task_details_record_ll.visibility = View.VISIBLE
                                }
                                "02" -> {
                                    abnormalType = "02"
                                    task_details_record_have.isChecked = true
                                    task_details_record_ll.visibility = View.VISIBLE
                                }
                            }
                        }

                        task_details_picll.removeAllViews()//重置图片数据
                        imageList.clear()

                        for (img in mut["imageUrl"] as List<MutableMap<String, Any>>) {
                            Log.i("asd", img.toString())
                            addImage(null, UP_IMAGE + img["wjmc"].toString(), img["fb1id"].toString())
                        }
                        task_details_soull.removeAllViews()
                        soundList.clear()
                        for (sound in mut["soundUrl"] as List<MutableMap<String, Any>>) {
                            addSound(null, UP_SOUND + sound["wjmc"].toString(), sound["fb1id"].toString())
                        }
                    } else {
                        (!"message").toast(menuActivity)
                    }
                    isfjxxpkid = true
                }
            }

        }

    }


    fun setSimple() {
        val han = Handler()
        han.postDelayed({
            task_details_ll_1.visibility = View.GONE
            task_details_ll_2.visibility = View.GONE
            task_details_ll_3.visibility = View.GONE
            task_details_ll_4.visibility = View.GONE
        }, 300)
        isSimple = true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> {
                val uri = getCameraUri()
                if (uri?.path != null) {
                    var upImage: File
                    val img = uri?.getCameraImg(menuActivity)
                    if (img == null) {
                        return
                    }
                    upImage = saveBitmapFile(img!!, menuActivity.filesDir.absolutePath + "imgcamera.jpg")
//                    upImage = saveBitmap(img!!, menuActivity.filesDir.absolutePath + "imgcamera.jpg")
                    addImage(upImage, "", "").setImageBitmap(img)
                    upLoadImage(upImage, 1)
                } else {
                    "uri==null".log("uuu")
                }
            }
            GALLERY_REQUEST -> {
                val uri = data?.data
//                val file=uri?.toFile(menuActivity)
                val img = uri?.handleImageOnKitKat(menuActivity)
                if (img == null) {
                    return
                }
                var upImage: File
                upImage = saveBitmapFile(img!!, menuActivity.filesDir.absolutePath + "imggallery.jpg")
//                upImage = saveBitmap(img!!, menuActivity.filesDir.absolutePath + "imggallery.jpg")
                addImage(upImage, "", "").setImageBitmap(img)
                upLoadImage(upImage, 1)
            }
            SOUND -> {
                val uri = data?.data
                if (uri == null) {
                    return
                }
                addSound(uri, null, "")
                upLoadImage(uri?.toFile(menuActivity), 2)
            }
        }
    }

    fun addSound(uri: Uri?, soundUrl: String?, id: String): Unit {
        val newImg = ImageView(menuActivity)
        newImg.setImageResource(R.mipmap.voice)
        newImg.setPadding(24, 24, 24, 24)
        newImg.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.reset()
            if (uri == null) {
                mediaPlayer.setDataSource(soundUrl)
            } else {
                mediaPlayer.setDataSource(menuActivity, uri)
            }
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            } else {
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
        newImg.setOnLongClickListener {
            AlertDialog.Builder(menuActivity)//删除确认框
                    .setTitle("")
                    .setMessage("是否删除")
                    .setNegativeButton("是") { dialog, id ->
                        removeFile(it as ImageView, soundList.get(it.getTag(R.id.image_id).toString().toInt()), 2)
                        isOnce = true
                    }
                    .setPositiveButton("否") { dialog, id -> }
                    .setCancelable(false)
                    .show()
//            "长按删除语音".toast(menuActivity)
            return@setOnLongClickListener true
        }
        newImg.setTag(R.id.image_id, soundList.size)
        if (uri == null) {
            soundList.add(FileInfo(null, soundUrl, id))
        } else {
            soundList.add(FileInfo(uri?.toFile(menuActivity), "", id))
        }
        task_details_soull.addView(newImg)
    }

    fun addImage(file: File?, imageURL: String?, id: String): ImageView {
        val newImg = ImageView(menuActivity)
        val lp = ViewGroup.LayoutParams(menuActivity.resources.getDimension(R.dimen.dp_48).toInt() + menuActivity.resources.getDimension(R.dimen.dp_16).toInt()
                , menuActivity.resources.getDimension(R.dimen.dp_48).toInt() + menuActivity.resources.getDimension(R.dimen.dp_16).toInt())
        newImg.setLayoutParams(lp);
        newImg.setPadding(menuActivity.resources.getDimension(R.dimen.dp_8).toInt(),
                menuActivity.resources.getDimension(R.dimen.dp_8).toInt(),
                menuActivity.resources.getDimension(R.dimen.dp_8).toInt(),
                menuActivity.resources.getDimension(R.dimen.dp_8).toInt())
        newImg.scaleType = ImageView.ScaleType.CENTER_INSIDE
        newImg.setTag(R.id.image_id, imageList.size)
        newImg.setOnClickListener {
            imgPopupWindow = (it as ImageView).showFullWindow()
        }
        newImg.setOnLongClickListener {
            AlertDialog.Builder(menuActivity)//删除确认框
                    .setTitle("")
                    .setMessage("是否删除")
                    .setNegativeButton("是") { dialog, id ->
                        removeFile(it as ImageView, imageList.get(it.getTag(R.id.image_id).toString().toInt()), 1)
                        isOnce = true
                    }
                    .setPositiveButton("否") { dialog, id -> }
                    .setCancelable(false)
                    .show()
//            "长按删除图片".toast(menuActivity)
            return@setOnLongClickListener true
        }
        if (file == null) {
            Glide.with(menuActivity).load(imageURL.toString())
//                    .placeholder(R.mipmap.picture)
                    .error(R.mipmap.picture)
//                    .dontAnimate()
                    .into(newImg)
            imageList.add(FileInfo(null, imageURL, id))
        } else {
            imageList.add(FileInfo(file, "", id))
        }
        task_details_picll.addView(newImg)
        return newImg
    }

    fun removeFile(view: ImageView, string: FileInfo, type: Int) {
        val dialog = LoadingDialog(menuActivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show()
        Http.upfile {
            url = BASEURL + DELET_FILE
            "fb1pkid" - string.fileId
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    "删除成功".toast(menuActivity)
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
                menuActivity.runOnUiThread {
                    "删除失败".toast(menuActivity)
                    dialog.dismiss()
                }
            }
        }
    }

    fun upLoadImage(path: File?, type: Int): Unit {
        val dialog = LoadingDialog(menuActivity);
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
                    "上传成功".toast(menuActivity)
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
        var uploadDialog: AlertDialog.Builder = AlertDialog.Builder(menuActivity)
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
        uploadDialog.show()
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
            i++
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
        if (abnormalType.equals("00")) {//状态正常时不上传
            return
        }
        if (fjxxpkid.equals("")) {
            isfjxxpkid = false
        }

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
            "czlx" - CZLX
            "zbpkid" - zbpkids
            success {
                menuActivity.runOnUiThread {
                    if (fjxxpkid.equals("")) {//如果没有附件信息id上传后刷新
                        isOnce = true
                        if (isSimple) {
                            initSimpleList()
                        } else {
                            initList()
                        }
                    }
                }

            }
            fail {
                menuActivity.runOnUiThread {
                    "网络错误".toast(menuActivity)
                }
            }
        }
    }

    /**
     * 把batmap 转file
     * @param bitmap
     * @param filepath
     */
    fun saveBitmapFile(bitmap: Bitmap, filepath: String): File {
        val file = File(filepath)//将要保存图片的路径
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    /**
     * bitmap保存为本地图片
     */
    fun saveBitmap(mBitmap: Bitmap, url: String): File {
//        mBitmap = ImageUtil.compressForScale(mBitmap);
        val f = File(url)
        if (f.exists()) {
            f.delete()
        }
        f.createNewFile()
        val baos = ByteArrayOutputStream()
        /* options表示 如果不压缩是100，表示压缩率为0。如果是70，就表示压缩率是70，表示压缩30%; */
        var options = 100
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().size / 1024 > 400) {
            // 循环判断如果压缩后图片是否大于500kb继续压缩
            baos.reset()
            options -= 20
            // 这里压缩options%，把压缩后的数据存放到baos中
            mBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            val out = FileOutputStream(f);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
        } catch (e: IOException) {
            e.printStackTrace();
        } finally {
//            mBitmap.recycle();
        }
        return f
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
//            saveAll()
            if (imgPopupWindow != null) {
                imgPopupWindow?.dismiss()
            }

        }
        if (isSimple) {
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "异常信息"
            }
        } else {
            (activity as MenuActivity).style {
                textBar = ""
                titleBar = "任务详情"
            }
        }

    }

    override fun onDestroy() {
        isDear = true
        saveAll()
        if (imgPopupWindow != null) {
            imgPopupWindow?.dismiss()
        }
//        overDialog.setCanceledOnTouchOutside(false);
//        overDialog.show();
        super.onDestroy()
    }
}