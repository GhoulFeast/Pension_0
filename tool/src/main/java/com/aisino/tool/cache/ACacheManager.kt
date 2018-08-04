package com.aisino.tool.cache

import java.io.File
import java.util.Collections
import java.util.HashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by lenovo on 2017/12/4.
 */

class ACacheManager(protected var cacheDir: File, private val sizeLimit: Long, private val countLimit: Int) {

    private val cacheSize: AtomicLong
    private val cacheCount: AtomicInteger
    private val lastUsageDates = Collections
            .synchronizedMap(HashMap<File, Long>())

    init {
        cacheSize = AtomicLong()
        cacheCount = AtomicInteger()
        calculateCacheSizeAndCacheCount()
    }

    /**
     * 计算 cacheSize和cacheCount
     */
    protected fun calculateCacheSizeAndCacheCount() {
        Thread(Runnable {
            var size = 0
            var count = 0
            val cachedFiles = cacheDir.listFiles()
            if (cachedFiles != null) {
                for (cachedFile in cachedFiles) {
                    size += calculateSize(cachedFile).toInt()
                    count += 1
                    lastUsageDates.put(cachedFile,
                            cachedFile.lastModified())
                }
                cacheSize.set(size.toLong())
                cacheCount.set(count)
            }
        }).start()
    }

    fun put(file: File) {
        var curCacheCount = cacheCount.get()
        while (curCacheCount + 1 > countLimit) {
            val freedSize = removeNext()
            cacheSize.addAndGet(-freedSize)

            curCacheCount = cacheCount.addAndGet(-1)
        }
        cacheCount.addAndGet(1)

        val valueSize = calculateSize(file)
        var curCacheSize = cacheSize.get()
        while (curCacheSize + valueSize > sizeLimit) {
            val freedSize = removeNext()
            curCacheSize = cacheSize.addAndGet(-freedSize)
        }
        cacheSize.addAndGet(valueSize)

        val currentTime = System.currentTimeMillis()
        file.setLastModified(currentTime)
        lastUsageDates.put(file, currentTime)
    }

    operator fun get(key: String): File {
        val file = newFile(key)
        val currentTime = System.currentTimeMillis()
        file.setLastModified(currentTime)
        lastUsageDates.put(file, currentTime)

        return file
    }

    fun newFile(key: String): File {
        return File(cacheDir, key.hashCode().toString() + "")
    }

    fun remove(key: String): Boolean {
        val image = get(key)
        return image.delete()
    }

    fun clear() {
        lastUsageDates.clear()
        cacheSize.set(0)
        val files = cacheDir.listFiles()
        if (files != null) {
            for (f in files) {
                f.delete()
            }
        }
    }

    /**
     * 移除旧的文件
     *
     * @return
     */
    protected fun removeNext(): Long {
        if (lastUsageDates.isEmpty()) {
            return 0
        }

        var oldestUsage: Long = 0L
        var mostLongUsedFile: File? = null
        val entries = lastUsageDates.entries
        synchronized(lastUsageDates) {
            for ((key, lastValueUsage) in entries) {
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = key
                    oldestUsage = lastValueUsage
                } else {
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage
                        mostLongUsedFile = key
                    }
                }
            }
        }

        val fileSize = calculateSize(mostLongUsedFile)
        if (mostLongUsedFile?.delete()!!) {
            lastUsageDates.remove(mostLongUsedFile)
        }
        return fileSize
    }

    protected fun calculateSize(file: File?): Long {
        return file!!.length()
    }
}
