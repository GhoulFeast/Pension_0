package com.aisino.tool

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by lenovo on 2017/11/14.
 * 开发用
 */


fun String.log(): Unit {
    Log.i("tag", "tag\t$this")
}

fun String.toast(context: Context): Unit {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.promptError(describe: String): Unit {
    Log.e("promptError", "File\t" + getFileName() + "\n"
            + "Class\t" + getClassName() + "\n"
            + "Method\t" + getMethodName() + "\n" + "Line\t" + getLineNumber() + "\n$this\n$describe")
}


fun getFileName(): String {
    return Thread.currentThread().stackTrace[2].fileName
}

fun getClassName(): String {
    return Thread.currentThread().stackTrace[2].className
}

fun getMethodName(): String {
    return Thread.currentThread().stackTrace[2].methodName
}

fun getLineNumber(): Int {
    return Thread.currentThread().stackTrace[2].lineNumber
}


