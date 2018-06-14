package com.aisino.tool.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import java.io.ByteArrayOutputStream

/**
 * Created by lenovo on 2017/12/4.
 */

object ACacheTool {

    internal val mSeparator = ' '


    /**
     * 判断缓存的String数据是否到期
     *
     * @param str
     * @return true：到期了 false：还没有到期
     */
    internal fun isDue(str: String): Boolean {
        return isDue(str.toByteArray())
    }

    /**
     * 判断缓存的byte数据是否到期
     *
     * @param data
     * @return true：到期了 false：还没有到期
     */
    internal fun isDue(data: ByteArray): Boolean {
        val strs = getDateInfoFromDate(data)
        if (strs != null && strs.size == 2) {
            var saveTimeStr = strs[0]
            while (saveTimeStr.startsWith("0")) {
                saveTimeStr = saveTimeStr
                        .substring(1, saveTimeStr.length)
            }
            val saveTime = java.lang.Long.valueOf(saveTimeStr)!!
            val deleteAfter = java.lang.Long.valueOf(strs[1])!!
            if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                return true
            }
        }
        return false
    }

    internal fun newStringWithDateInfo(second: Int, strInfo: String): String {
        return createDateInfo(second) + strInfo
    }

    internal fun newByteArrayWithDateInfo(second: Int, data2: ByteArray): ByteArray {
        val data1 = createDateInfo(second).toByteArray()
        val retdata = ByteArray(data1.size + data2.size)
        System.arraycopy(data1, 0, retdata, 0, data1.size)
        System.arraycopy(data2, 0, retdata, data1.size, data2.size)
        return retdata
    }

    internal fun clearDateInfo(strInfo: String?): String? {
        var strInfo = strInfo
        if (strInfo != null && hasDateInfo(strInfo.toByteArray())) {
            strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1, strInfo.length)
        }
        return strInfo
    }

    internal fun clearDateInfo(data: ByteArray): ByteArray {
        return if (hasDateInfo(data)) {
            copyOfRange(data, indexOf(data, mSeparator) + 1,
                    data.size)
        } else data
    }

    internal fun hasDateInfo(data: ByteArray?): Boolean {
        return (data != null && data.size > 15 && data[13].toChar() == '-'
                && indexOf(data, mSeparator) > 14)
    }

    internal fun getDateInfoFromDate(data: ByteArray): Array<String>? {
        if (hasDateInfo(data)) {
            val saveDate = String(copyOfRange(data, 0, 13))
            val deleteAfter = String(copyOfRange(data, 14,
                    indexOf(data, mSeparator)))
            return arrayOf(saveDate, deleteAfter)
        }
        return null
    }

    internal fun indexOf(data: ByteArray, c: Char): Int {
        for (i in data.indices) {
            if (data[i] == c.toByte()) {
                return i
            }
        }
        return -1
    }

    internal fun copyOfRange(original: ByteArray, from: Int, to: Int): ByteArray {
        val newLength = to - from
        if (newLength < 0)
            throw IllegalArgumentException(from.toString() + " > " + to)
        val copy = ByteArray(newLength)
        System.arraycopy(original, from, copy, 0,
                Math.min(original.size - from, newLength))
        return copy
    }

    internal fun createDateInfo(second: Int): String {
        var currentTime = System.currentTimeMillis().toString() + ""
        while (currentTime.length < 13) {
            currentTime = "0" + currentTime
        }
        return currentTime + "-" + second + mSeparator
    }

    /*
     * Bitmap → byte[]
     */
    internal fun Bitmap2Bytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    /*
     * byte[] → Bitmap
     */
    internal fun Bytes2Bimap(b: ByteArray?): Bitmap? {
        return if (b?.size == 0) {
            null
        } else BitmapFactory.decodeByteArray(b, 0, b!!.size)
    }

    /*
     * Drawable → Bitmap
     */
    internal fun drawable2Bitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        // 取 drawable 的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888
        else
            Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    /*
     * Bitmap → Drawable
     */
    internal fun bitmap2Drawable(bm: Bitmap?): Drawable? {
        return if (bm == null) {
            null
        } else BitmapDrawable(bm)
    }
}
