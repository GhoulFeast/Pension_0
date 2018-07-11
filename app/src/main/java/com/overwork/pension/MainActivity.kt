package com.overwork.pension

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.LOGIN
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewAndEvent()
    }

    fun initViewAndEvent(): Unit {
        lg_login.setOnClickListener{
            toLogin()
            startActivity(Intent(this@MainActivity,MenuActivity::class.java))

        }
    }

    fun toLogin(): Unit {
        if (lg_name.text.toString().equals("")){
            ToastAdd.showToast_w(this,"请输入用户名")
//            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show()
            return
        }
        if (lg_pwd.text.toString().equals("")){
            ToastAdd.showToast_w(this,"请输入密码")
//            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show()
            return
        }
        Http.get{
            url= BASEURL+ LOGIN

            "yhmc"-lg_name.text.toString()

            "yhmm"-lg_pwd.text.toString()

            success {
                if ((!"code").toInt()==2000){
                    userId= "result".."userId"
                    userType="result".."userType"
                    userName="result".."userName"
                    userLevel="result".."userLevel"
                    userLevelName="result".."userLevelName"
                    entryTime="result".."entryTime"
                    workingYears="result".."workingYears"
                    superiorName="result".."superiorName"
                    userPortrait="result".."userPortrait"
                    startActivity(Intent(this@MainActivity,MenuActivity::class.java))
                }
            }



        }
    }
}
