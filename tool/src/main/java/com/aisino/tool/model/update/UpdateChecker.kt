package com.aisino.tool.model.update

import android.app.Activity
import android.content.Context
import android.util.Log

object UpdateChecker {

    fun checkForDialog(context: Activity?,upMsg:String,updateUrl: String) {
        if (context != null) {
            UpdateDialog.show(context, upMsg, updateUrl)
        } else {

        }
    }


    fun checkForNotification(context: Context?,upMsg:String, updateUrl: String) {
        if (context != null) {
            NotificationHelper(context).showNotification(upMsg, updateUrl)
        } else {

        }
    }
}
