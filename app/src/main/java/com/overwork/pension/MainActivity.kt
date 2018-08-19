package com.overwork.pension

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.aisino.tool.DEBUG
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.log
import com.aisino.tool.system.getAllPermissions
import com.aisino.tool.system.signPermissions
import com.hq.kbase.network.Http
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.LOGIN
import com.overwork.pension.other.*
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var hasName = false
    var hasPwd = false
    var isExit=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewAndEvent()
        CrashReport.initCrashReport(application, "ce4e77be1a", true);
        DEBUG(true)

    }

    fun initViewAndEvent(): Unit {
        isExit= intent.getBooleanExtra("exit",false)
        if (isExit){
            var share = getPreferences(Context.MODE_PRIVATE)
            var edio = share.edit()
            edio.putString("yhmc", "")
            edio.putString("yhmm", "")
            edio.commit()
        }
        lg_login.setOnClickListener {
            if (hasName && hasPwd) {
                toLogin()
            }
//            startActivity(Intent(this@MainActivity,MenuActivity::class.java))

        }
        lg_login.alpha = 0.3f
        lg_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ed: Editable?) {
                hasName=!ed.isNullOrBlank()
                if (hasName && hasPwd) {
                    lg_login.alpha = 1.0f
                } else {
                    lg_login.alpha = 0.3f
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        lg_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ed: Editable?) {
                hasPwd=!ed.isNullOrBlank()
                if (hasName && hasPwd) {
                    lg_login.alpha = 1.0f
                } else {
                    lg_login.alpha = 0.3f
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        signPermissions(getAllPermissions(this)!!)
        //自动登陆
        var share = getPreferences(Context.MODE_PRIVATE)

        lg_name.setText(share.getString("yhmc", ""))
        lg_pwd.setText(share.getString("yhmm", ""))
        if (!lg_name.text.toString().equals("")) {
            toLogin()
        }
    }

    fun toLogin(): Unit {
        val dialog = LoadingDialog(this)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
//        if (lg_name.text.toString().equals("")){
////            ToastAdd.showToast(this,"请输入用户名")
//            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (lg_pwd.text.toString().equals("")){
////            ToastAdd.showToast(this,"请输入密码")
//            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show()
//            return
//        }
        Http.post {
            url = BASEURL + LOGIN
            "userAccount" - lg_name.text.toString()
            "userPassword" - lg_pwd.text.toString()
            success {
                if ((!"status").toInt() == 200) {
                    userId = "result".."userId"
                    userType = "result".."userType"
                    userName = "result".."userName"
                    userLevelName = "result".."userLevelName"
                    entryTime = "result".."entryTime"
                    workingYears = "result".."workingYears"
                    superiorName = "result".."superiorName"
                    userPortrait = "result".."userPortrait"
                    startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                    var share = getPreferences(Context.MODE_PRIVATE)
                    var edio = share.edit()
                    edio.putString("yhmc", lg_name.text.toString())
                    edio.putString("yhmm", lg_pwd.text.toString())
                    edio.commit()
                    finish()
                } else {
                    val msg: String = !"message"
                    runOnUiThread { Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show() }
                }
                dialog.dismiss()
            }
            fail {
                dialog.dismiss()
            }
        }
    }
}
