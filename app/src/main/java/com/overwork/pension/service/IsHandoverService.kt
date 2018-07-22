package com.overwork.pension.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hq.kbase.network.Http
import com.overwork.pension.other.AUTO_UPDATE_MSG
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.IS_HANDOVER
import com.overwork.pension.other.userId
import java.util.*

class IsHandoverService : Service() {
    val timer = Timer()
    var isRun=false
    lateinit var wcCall:IsHandoverCall


    override fun onBind(p0: Intent?): IBinder {
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intevalPeriod: Long = 10000
        val timerTask = object : TimerTask() {
            override fun run() {
                if (isRun){
                    Http.get {
                        url = BASEURL + IS_HANDOVER

                        "userId" - userId

                        success {
                            wcCall.setMsgNum(("result".."isHandove"))

                        }

                        fail { }
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
            this@IsHandoverService.isRun = isrun
        }

         fun setCallBack (call:IsHandoverCall){
             wcCall=call
         }
    }

    interface IsHandoverCall {
        fun setMsgNum(num:String)
    }

}