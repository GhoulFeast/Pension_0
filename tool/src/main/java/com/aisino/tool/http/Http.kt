package com.hq.kbase.network

import android.content.Context
import android.net.ConnectivityManager
import com.aisino.tool.discreteness.StreamActivity.app.mApplication

/**
 * Created by lenovo on 2017/11/14.
 */
object Http {
    val sub= Submit()

    var get = fun(function: Submit.() -> Unit) {

        sub.method=Method.GET
        sub.function()
        sub.run()

    }

    var post = fun(function: Submit.() -> Unit) {
//        val sub= Submit()
        sub.method=Method.POST
        sub.function()
        sub.run()
    }

    var upload = fun(function: Submit.() -> Unit) {
//        val sub= Submit()
        sub.function()
        sub.method=Method.IMAGE
        sub.run()
    }

    var download = fun(function: Submit.() -> Unit) {
//        val sub= Submit()
        sub.function()
        sub.method=Method.DOWNLOAD
        sub.run()
    }

}

