package com.overwork.pension.other

import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast


import com.overwork.pension.R

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class SelectPicPopupWindow : Activity(), OnClickListener {
    private var btn_take_photo: Button? = null
    private var btn_pick_photo: Button? = null
    private var btn_cancel: Button? = null
    private var layout: LinearLayout? = null
    // getExternalStorageDirectory,下面这句指定调用相机拍照后的照片存储的路径
    internal var tempFile = File(cacheDir,
            photoFileName)

    // 获得照片的文件名称
    private val photoFileName: String
        get() {
            val date = Date(System.currentTimeMillis())
            val dateFormat = SimpleDateFormat(
                    "'IMG'_yyyyMMdd_HHmmss")
            return dateFormat.format(date) + ".jpg"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_dialog)
        intent = getIntent()
        btn_take_photo = this.findViewById<View>(R.id.btn_take_photo) as Button// 照相
        btn_pick_photo = this.findViewById<View>(R.id.btn_pick_photo) as Button// 相册
        btn_cancel = this.findViewById<View>(R.id.btn_cancel) as Button

        layout = findViewById<View>(R.id.pop_layout) as LinearLayout

        // 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout!!.setOnClickListener {
            Toast.makeText(applicationContext, "提示：点击窗口外部关闭窗口！",
                    Toast.LENGTH_SHORT).show()
        }
        // 添加按钮监听
        btn_cancel!!.setOnClickListener(this)
        btn_pick_photo!!.setOnClickListener(this)
        btn_take_photo!!.setOnClickListener(this)
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    override fun onTouchEvent(event: MotionEvent): Boolean {
        finish()
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_take_photo -> try {
                // 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
                // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                //指定拍照后的缓存路径(Uri)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile))
                startActivityForResult(intent, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            R.id.btn_pick_photo -> try {
                // 选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 2)
            } catch (e: ActivityNotFoundException) {

            }

            R.id.btn_cancel -> finish()
            else -> {
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var data = data
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
        // 如果是调用相机拍照时
            1 -> {
                //                startPhotoZoom(Uri.fromFile(tempFile), 150);
                data = Intent()
                data.data = Uri.fromFile(tempFile)
                setPicToView(data)
            }
        // 如果是直接从相册获取
            2 -> if (data != null) {
                if (Build.VERSION.SDK_INT >= 19) {
                    if (DocumentsContract.isDocumentUri(
                                    this@SelectPicPopupWindow, data.data)) {
                        var imagePath: String? = null
                        val wholeID = DocumentsContract.getDocumentId(data
                                .data)
                        val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        val column = arrayOf(MediaColumns.DATA)
                        val sel = BaseColumns._ID + "=?"
                        val cursor = this@SelectPicPopupWindow
                                .contentResolver
                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        column, sel, arrayOf(id), null)
                        val columnIndex = cursor!!.getColumnIndex(column[0])
                        if (cursor.moveToFirst()) {
                            imagePath = cursor.getString(columnIndex)
                        }
                        cursor.close()
                        val newUri = Uri.parse("file:///" + imagePath!!) // 将绝对路径转换为URL
                        startPhotoZoom(newUri, 100)
                    } else {
                        startPhotoZoom(data.data, 100)
                    }
                } else {
                    startPhotoZoom(data.data, 100)
                }

            }
            3 -> {
                Log.e("zoom", "begin2")
                if (data != null)
                    setPicToView(data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startPhotoZoom(uri: Uri?, size: Int) {
        Log.e("zoom", "begin")
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true")

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)

        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", size)
        intent.putExtra("outputY", size)
        intent.putExtra("return-data", true)
        Log.e("zoom", "begin1")
        startActivityForResult(intent, 3)
    }

    // 保存裁剪之后的图片数据
    private fun setPicToView(data: Intent) {
        // 选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
        if (data.extras != null)
            intent!!.putExtras(data.extras!!)
        if (data.data != null)
            intent!!.data = data.data
        println(data)
        setResult(1, intent)
        finish()

    }

}
