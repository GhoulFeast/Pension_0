package com.aisino.tool

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast

/**
 * Created by lenovo on 2017/11/14.
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


/**
 * 获取当前程序的版本号
 */
fun Activity.getVersionCode(): Int {
    val manager = this.application.getPackageManager()
    try {
        val info = manager.getPackageInfo(
                this.application.getPackageName(), 0)
        val versionCode = info.versionCode
//        Log.i(TAG, "versionCode = " + versionCode)
        return versionCode
    } catch (e: PackageManager.NameNotFoundException) {
    }

    return 1
}

/**
 * 获取当前程序的版本名称
 */
fun Activity.getVersionName(): String {
    val manager = this.application.getPackageManager()
    try {
        val info = manager.getPackageInfo(
                this.application.getPackageName(), 0)
//        Log.i(TAG, "pinfo.versionName = " + info.versionName)
        return info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
    }

    return "1.0"
}

/**
 * 获取手机IMEI
 */
fun Activity.getIMEICode(): String {
    val telephonyManager = this.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var IMEI = telephonyManager.deviceId
    if (IMEI == null) {
        IMEI = Settings.System.getString(this.application.getContentResolver(),
                Settings.System.ANDROID_ID)
    }
    return IMEI
}

fun Activity.getOperator(): String {
    val telephonyManager = this.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var operator = telephonyManager.simOperatorName
    if (operator == null) {
        operator = telephonyManager.networkOperatorName
    }
    return operator
}

/**
 * 打开手机 网络设置界面
 */
fun Activity.openNetworkSettings() {
    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
    this.startActivity(intent)
}

/**
 * 判断网络连接
 */
fun Activity.isNetworkAvailable(): Boolean {
    val connectivity = this.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connectivity?.allNetworkInfo
    if (info != null) {
        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
    }
    return false
}

/**
 * 获取SDK版本
 */
fun Activity.getSdkVersion(): Int {
    return Build.VERSION.SDK_INT
}

/**
 * 获取手机屏幕宽度
 */
fun Activity.getScreenWidth(): Int {
    val display = (this.application
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    return display.width
}

/**
 * 获取手机屏幕高度
 */
fun Activity.getScreenHeight(): Int {
    val display = (this.application
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    return display.height
}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Activity.dip2px(dpValue: Float): Int {
    val scale = this.getResources().getDisplayMetrics().density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Activity.px2dip(pxValue: Float): Int {
    val scale = this.getResources().getDisplayMetrics().density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 判断手机定位
 */
fun Activity.isLocationEnabled(): Boolean {
    val manager = this.application
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (manager != null) {
        val gps = manager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
        val net = manager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || net
    }
    return false
}
