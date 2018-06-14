package com.aisino.tool.system

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.aisino.tool.bitmap.drawable2Bitmap
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by lenovo on 2017/12/5.
 */
val CAMERA_REQUEST = 100;
val GALLERY_REQUEST = 200;


fun Activity.openGallery() {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    this.startActivityForResult(intent, GALLERY_REQUEST)
}

fun Activity.openCamera(): Uri {
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
    return contentUri
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