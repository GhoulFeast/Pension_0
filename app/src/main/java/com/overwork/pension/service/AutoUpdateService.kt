package com.overwork.pension.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hq.kbase.network.Http
import com.overwork.pension.other.*
import com.overwork.pension.other.userId
import java.util.*



class AutoUpdateService :Service() {
    val timer = Timer()
    var isRun=false
    lateinit var wcCall:AutoUpdateCall


    override fun onBind(p0: Intent?): IBinder {
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intevalPeriod: Long = 20000
        val timerTask = object : TimerTask() {
            override fun run() {
                if (isRun){
                    Http.get{
                        url=BASEURL+ AUTO_UPDATE_MSG

                        "userId"- userId

                        success {
                                wcCall.setMsgNum(("result".."hasNew"))

                        }

                        fail {  }
                    }

                }
//                LogUtil.d(isRun.toString()+">>>>>")

            }
        }

        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(timerTask,0, intevalPeriod)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        timer.cancel()
    }

    inner class Binder : android.os.Binder() {
        fun setRun(isrun: Boolean) {
            this@AutoUpdateService.isRun = isrun
        }

         fun setCallBack (call:AutoUpdateCall){
             wcCall=call
         }
    }

    interface AutoUpdateCall {
        fun setMsgNum(num:String)
    }

}