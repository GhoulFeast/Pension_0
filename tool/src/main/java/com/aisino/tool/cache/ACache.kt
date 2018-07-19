package com.aisino.tool.cache

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable

import org.json.JSONArray
import org.json.JSONObject

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.RandomAccessFile
import java.io.Serializable
import java.util.HashMap

/**
 * Created by lenovo on 2017/12/4.
 */

class ACache (cacheDir: File, max_size: Long, max_count: Int) {
    private val mCache: ACacheManager
    private val tool: ACacheTool


    init {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw RuntimeException("can't make dirs in " + cacheDir.absolutePath)
        }
        mCache = ACacheManager(cacheDir, max_size, max_count)
        tool= ACacheTool()
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================
    /**
     * 保存 String数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的String数据
     */
    fun put(key: String, value: String) {
        val file = mCache.newFile(key)
        var out: BufferedWriter? = null
        try {
            out = BufferedWriter(FileWriter(file), 1024)
            out.write(value)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            mCache.put(file)
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的String数据
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: String, saveTime: Int) {
        put(key,  tool.newStringWithDateInfo(saveTime, value))
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    fun getAsString(key: String): String? {
        val file = mCache[key]
        if (!file.exists())
            return null
        var removeFile = false
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(file))
            var readString = ""
            var currentLine = ""
            while (currentLine != null) {
                readString += currentLine
                currentLine = br.readLine()
            }
            if (! tool.isDue(readString)) {
                return  tool.clearDateInfo(readString)
            } else {
                removeFile = true
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (removeFile)
                remove(key)
        }
    }

    // =======================================
    // ============= JSONObject 数据 读写 ==============
    // =======================================
    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的JSON数据
     */
    fun put(key: String, value: JSONObject) {
        put(key, value.toString())
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的JSONObject数据
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: JSONObject, saveTime: Int) {
        put(key, value.toString(), saveTime)
    }

    /**
     * 读取JSONObject数据
     *
     * @param key
     * @return JSONObject数据
     */
    fun getAsJSONObject(key: String): JSONObject? {
        val JSONString = getAsString(key)
        try {
            return JSONObject(JSONString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================
    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的JSONArray数据
     */
    fun put(key: String, value: JSONArray) {
        put(key, value.toString())
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的JSONArray数据
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: JSONArray, saveTime: Int) {
        put(key, value.toString(), saveTime)
    }

    /**
     * 读取JSONArray数据
     *
     * @param key
     * @return JSONArray数据
     */
    fun getAsJSONArray(key: String): JSONArray? {
        val JSONString = getAsString(key)
        try {
            return JSONArray(JSONString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================
    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的数据
     */
    fun put(key: String, value: ByteArray) {
        val file = mCache.newFile(key)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            out.write(value)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            mCache.put(file)
        }
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的数据
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: ByteArray, saveTime: Int) {
        put(key,  tool.newByteArrayWithDateInfo(saveTime, value))
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    fun getAsBinary(key: String): ByteArray? {
        var RAFile: RandomAccessFile? = null
        var removeFile = false
        try {
            val file = mCache[key]
            if (!file.exists())
                return null
            RAFile = RandomAccessFile(file, "r")
            val byteArray = ByteArray(RAFile.length().toInt())
            RAFile.read(byteArray)
            if (! tool.isDue(byteArray)) {
                return  tool.clearDateInfo(byteArray)
            } else {
                removeFile = true
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (removeFile)
                remove(key)
        }
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的value
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: Serializable, saveTime: Int = -1) {
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(value)
            val data = baos.toByteArray()
            if (saveTime != -1) {
                put(key, data, saveTime)
            } else {
                put(key, data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                oos!!.close()
            } catch (e: IOException) {
            }

        }
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    fun getAsObject(key: String): Any? {
        val data = getAsBinary(key)
        if (data != null) {
            var bais: ByteArrayInputStream? = null
            var ois: ObjectInputStream? = null
            try {
                bais = ByteArrayInputStream(data)
                ois = ObjectInputStream(bais)
                return ois.readObject()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            } finally {
                try {
                    if (bais != null)
                        bais.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    if (ois != null)
                        ois.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================
    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的bitmap数据
     */
    fun put(key: String, value: Bitmap) {
        put(key,  tool.Bitmap2Bytes(value))
    }

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的 bitmap 数据
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: Bitmap, saveTime: Int) {
        put(key,  tool.Bitmap2Bytes(value), saveTime)
    }

    /**
     * 读取 bitmap 数据
     *
     * @param key
     * @return bitmap 数据
     */
    fun getAsBitmap(key: String): Bitmap? {
        return if (getAsBinary(key) == null) {
            null
        } else  tool.Bytes2Bimap(getAsBinary(key))
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================
    /**
     * 保存 drawable 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的drawable数据
     */
    fun put(key: String, value: Drawable) {
        put(key,  tool.drawable2Bitmap(value))
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key
     * 保存的key
     * @param value
     * 保存的 drawable 数据
     * @param saveTime
     * 保存的时间，单位：秒
     */
    fun put(key: String, value: Drawable, saveTime: Int) {
        put(key,  tool.drawable2Bitmap(value), saveTime)
    }

    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @return Drawable 数据
     */
    fun getAsDrawable(key: String): Drawable? {
        return if (getAsBinary(key) == null) {
            null
        } else  tool.bitmap2Drawable( tool.Bytes2Bimap(getAsBinary(key)))
    }

    /**
     * 获取缓存文件
     *
     * @param key
     * @return value 缓存的文件
     */
    fun file(key: String): File? {
        val f = mCache.newFile(key)
        return if (f.exists()) f else null
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    fun remove(key: String): Boolean {
        return mCache.remove(key)
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        mCache.clear()
    }

//    companion object {
//
//        val TIME_HOUR = 60 * 60
//        val TIME_DAY = TIME_HOUR * 24
//        private val MAX_SIZE = 1000 * 1000 * 50 // 50 mb
//        private val MAX_COUNT = Integer.MAX_VALUE // 不限制存放数据的数量
//        private val mInstanceMap = HashMap<String, ACache>()
//
//        operator fun get(ctx: Context, cacheName: String = "ACache"): ACache {
//            val f = File(ctx.cacheDir, cacheName)
//            return get(f, MAX_SIZE.toLong(), MAX_COUNT)
//        }
//
//        operator fun get(ctx: Context, max_zise: Long, max_count: Int): ACache {
//            val f = File(ctx.cacheDir, "ACache")
//            return get(f, max_zise, max_count)
//        }
//
//        operator fun get(cacheDir: File, max_zise: Long = MAX_SIZE.toLong(), max_count: Int = MAX_COUNT): ACache {
//            var manager: ACache? = mInstanceMap[cacheDir.absoluteFile.toString() + myPid()]
//            if (manager == null) {
//                manager = ACache(cacheDir, max_zise, max_count)
//                mInstanceMap.put(cacheDir.absolutePath + myPid(), manager)
//            }
//            return manager
//        }
//
//        private fun myPid(): String {
//            return "_" + android.os.Process.myPid()
//        }
//    }
}
lateinit var cache:ACache

fun Activity.getCache(): ACache {
    if (cache==null){
        cache= ACache(this.cacheDir, Long.MAX_VALUE, Int.MAX_VALUE)
    }
        return cache
}
