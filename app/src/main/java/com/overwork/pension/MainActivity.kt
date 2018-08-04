package com.overwork.pension

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.aisino.tool.ani.LoadingDialog
import com.aisino.tool.cache.ACache
import com.aisino.tool.cache.getCache
import com.aisino.tool.system.getAllPermissions
import com.aisino.tool.system.signPermissions
import com.aisino.tool.widget.ToastAdd
import com.hq.kbase.network.Http
import com.hq.kbase.network.isHttpWaitAni
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.other.BASEURL
import com.overwork.pension.other.LOGIN
import com.overwork.pension.other.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var hasName=false
    var hasPwd=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewAndEvent()

    }

    fun initViewAndEvent(): Unit {
        isHttpWaitAni=true

        lg_login.setOnClickListener{
            if (hasName&&hasPwd){
                toLogin()
            }
//            startActivity(Intent(this@MainActivity,MenuActivity::class.java))

        }
        lg_login.alpha=0.3f
        lg_name.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(ed: Editable?) {
                if (ed.isNullOrBlank()){
                    hasName=false
                }else{
                    hasName=true
                }
                if (hasName&&hasPwd){
                    lg_login.alpha=1.0f
                }else{
                    lg_login.alpha=0.3f
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        } )
        lg_pwd.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(ed: Editable?) {
                if (ed.isNullOrBlank()){
                    hasPwd=false
                }else{
                    hasPwd=true
                }
                if (hasName&&hasPwd){
                    lg_login.alpha=1.0f
                }else{
                    lg_login.alpha=0.3f
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        } )
        signPermissions(getAllPermissions(this)!!)
        //自动登陆
        lg_name.setText(getCache().getAsString("yhmc"))
        lg_pwd.setText(getCache().getAsString("yhmm"))
        if (!lg_name.text.toString().equals("")){
            toLogin()
        }
    }

    fun toLogin(): Unit {
        val dialog =  LoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
        Http.get{
            url= BASEURL+ LOGIN
            "yhmc"-lg_name.text.toString()
            "yhmm"-lg_pwd.text.toString()
            success {
                if ((!"status").toInt()==200){
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
                    getCache().put("yhmc",lg_name.text.toString())
                    getCache().put("yhmm",lg_pwd.text.toString())
                    finish()
                }else{
                    val msg:String="result".."msg"
                    runOnUiThread { Toast.makeText(this@MainActivity,msg,Toast.LENGTH_SHORT).show() }
                }
                dialog.dismiss()
            }
fail {
    dialog.dismiss()
}
            get()
        }
    }
}
