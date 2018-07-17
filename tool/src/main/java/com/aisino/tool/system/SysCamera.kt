package com.aisino.tool.system

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.aisino.tool.R
import com.aisino.tool.bitmap.drawable2Bitmap
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*



/**
 * Created by lenovo on 2017/12/5.
 */
val CAMERA_REQUEST = 1000
val GALLERY_REQUEST = 2000


fun Activity.openCameraAndGalleryWindow() {
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView = this.layoutInflater.inflate(R.layout.camera_gallery_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中

    val mPopupWindow = PopupWindow(mPopView,
            this.windowManager.defaultDisplay.width-128,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    var openCamera = mPopView.findViewById<Button>(R.id.btn_take_photo)
    var openGallery = mPopView.findViewById<Button>(R.id.btn_pick_photo)
    var cancel = mPopView.findViewById<Button>(R.id.btn_cancel)
    openCamera.setOnClickListener{
        val uri= this.openCamera()
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    openGallery.setOnClickListener{
        this.openGallery()
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    cancel.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    mPopupWindow.setOnDismissListener {
        val params = this.getWindow().getAttributes()
        params.alpha = 1f
        this.getWindow().setAttributes(params)
    }
    if (mPopupWindow.isShowing()) {
        mPopupWindow.dismiss();
    } else {
        val params = this.getWindow().getAttributes()
        params.alpha = 0.7f
        this.getWindow().setAttributes(params)
        // 设置PopupWindow 显示的形式 底部或者下拉等
        // 在某个位置显示
        mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
    }
}

fun Activity.openGallery() {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    this.startActivityForResult(intent, GALLERY_REQUEST)
}

fun Activity.openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    val imgName = year.toString() + "_" + month + "_" + day + "(" + System.currentTimeMillis() + ").jpg"
    val f = File(this.filesDir.path, imgName)
    val contentUri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        contentUri = FileProvider.getUriForFile(this, "com.aisino.tool.fileProvider", f)
        //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
        contentUri = Uri.fromFile(f)
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
    intent.putExtra("return-data", false);
    this.startActivityForResult(intent, CAMERA_REQUEST)
}


//读取uri
fun Uri.handleImageOnKitKat(activity: Activity): Bitmap? {
    var imagePath: String? = null
//        val uri = Uri.parse(uriString)
    if (DocumentsContract.isDocumentUri(activity, this)) {
        val docId = DocumentsContract.getDocumentId(this)
        if ("com.android.providers.media.documents" == this.authority) {
            val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val selection = MediaStore.Images.Media._ID + "=" + id
            imagePath = getImagePath(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
        } else if ("com.android.providers.downloads.documents" == this.authority) {
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)!!)
            imagePath = getImagePath(activity, contentUri, null)
        }
    } else if ("content".equals(this.scheme, ignoreCase = true)) {
        imagePath = getImagePath(activity, this, null)
    }
    return BitmapFactory.decodeFile(imagePath)
}

private fun getImagePath(activity: Activity, uri: Uri, selection: String?): String? {
    var path: String? = null
    val cursor = activity.contentResolver.query(uri, null, selection, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val num = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            path = cursor.getString(num)
        }

        cursor.close()
    }
    return path
}

fun Uri.getCameraImg(activity: Activity): Bitmap? {
    var stream: InputStream? = null
    val sCompatUseCorrectStreamDensity = activity.applicationInfo.targetSdkVersion > Build.VERSION_CODES.M

    try {
        stream = activity.contentResolver.openInputStream(this)
        val dar = Drawable.createFromResourceStream(if (sCompatUseCorrectStreamDensity) activity.resources else null, null, stream, null)
        return dar.drawable2Bitmap()
    } catch (e: Exception) {

    } finally {
        if (stream != null) {
            try {
                stream.close()
            } catch (e: IOException) {
            }

        }
    }
    return null
}

fun Uri.toFile(context: Context): File? {
    var path: String? = null
    if ("file" == this.scheme) {
        path = this.encodedPath
        if (path != null) {
            path = Uri.decode(path)
            val cr = context.getContentResolver()
            val buff = StringBuffer()
            buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'$path'").append(")")
            val cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA), buff.toString(), null, null)
            var index = 0
            var dataIdx = 0
            cur.moveToFirst()
            while (!cur.isAfterLast()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                index = cur.getInt(index)
                dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cur.getString(dataIdx)
                cur.moveToNext()
            }
            cur.close()
            if (index == 0) {
            } else {
                val u = Uri.parse("content://media/external/images/media/$index")
                println("temp uri is :$u")
            }
        }
        if (path != null) {
            return File(path)
        }
    } else if ("content" == this.scheme) {
        // 4.2.2以后
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.getContentResolver().query(this, proj, null, null, null)
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            path = cursor.getString(columnIndex)
        }
        cursor.close()

        return File(path!!)
    }
    return null
}