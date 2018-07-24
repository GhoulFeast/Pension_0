package com.overwork.pension.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import com.aisino.qrcode.activity.CaptureActivity
import com.aisino.tool.toast
import com.aisino.tool.widget.openUnterTheViewListWindow
import com.overwork.pension.R
import com.overwork.pension.fragment.*
import com.overwork.pension.other.UseFragmentManager
import com.overwork.pension.other.userType
import com.overwork.pension.service.AutoUpdateService
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity() , ServiceConnection {
   

    private var showFragment: Fragment? = null
    private val enety: MutableMap<String, Any> = mutableMapOf()
    private val fragments = ArrayList<Fragment>()
    private var nowState = 0
    private var backPressTime=0L
    private var auBinder: AutoUpdateService.Binder? = null
    private val QRCODE=999


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        initViewAndEvent()
    }

    fun initViewAndEvent(): Unit {
        main_rg.setOnCheckedChangeListener({ radioGroup, i ->
            when (i) {
                R.id.main_rb_homepage -> {
                    var homeFragment = HomeFragment()
                    UseFragmentManager.displayFragment(showFragment, homeFragment,
                            supportFragmentManager, R.id.main_ll)
                    showFragment = homeFragment
                }
                R.id.main_rb_class -> {
                    if (userType.toInt() == 2) {
                        startActivityForResult(Intent(this, CaptureActivity::class.java), -1)
                    } else {
                        var classFragment = ClassFragment()
                        UseFragmentManager.displayFragment(showFragment, classFragment,
                                supportFragmentManager, R.id.main_ll)
                        showFragment = classFragment
                    }


                }
                R.id.main_rb_msg -> {
                    var msgFragment = MsgFragment()
                    UseFragmentManager.displayFragment(showFragment, msgFragment,
                            supportFragmentManager, R.id.main_ll)
                    showFragment = msgFragment

                }
                R.id.main_rb_mine -> {
                    var mineFragment = MineFragment()
                    UseFragmentManager.displayFragment(showFragment, mineFragment,
                            supportFragmentManager, R.id.main_ll)
                    showFragment = mineFragment

                }
            }
            fragments.clear()
            nowState = 0
        })
        main_rb_homepage.performClick()
        title_back.setOnClickListener {
            backFragment()
        }
        bar_more.setOnClickListener{
           val list= openUnterTheViewListWindow(it,ArrayList<String>().apply { add("扫一扫") })
            list.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
                startActivityForResult(Intent(this@MenuActivity,CaptureActivity::class.java),QRCODE)
            }
        }
        val intent=Intent(this@MenuActivity, AutoUpdateService::class.java)
        bindService(intent, this@MenuActivity, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    public fun toHomePage() {
        main_rb_homepage.performClick()
    }

    override fun onPause() {
        super.onPause()
        auBinder?.setRun(false)
    }

    override fun onRestart() {
        super.onRestart()
        auBinder?.setRun(true)
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        auBinder = p1 as AutoUpdateService.Binder
        auBinder?.setRun(true)
        auBinder?.setCallBack(object : AutoUpdateService.AutoUpdateCall {
            override fun setMsgNum(num: String) {
                runOnUiThread {
                    if (num.toBoolean()){
                        var dra= resources.getDrawable(R.mipmap.msg_red)
                        dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight())
                        main_rb_msg.setCompoundDrawables(main_rb_msg.getCompoundDrawables()[0], dra, main_rb_msg.getCompoundDrawables()[2], main_rb_msg.getCompoundDrawables()[3])
                    }else{
                        var dra= resources.getDrawable(R.mipmap.msg)
                        dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
                        main_rb_msg.setCompoundDrawables(main_rb_msg.getCompoundDrawables()[0], dra, main_rb_msg.getCompoundDrawables()[2], main_rb_msg.getCompoundDrawables()[3])
                    }
                }
            }
        })
    }

    override fun onServiceDisconnected(p0: ComponentName?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == -1 && resultCode == Activity.RESULT_OK) {
                var handoverDiretor = HandoverDirectorFragment()
                var bd = Bundle()
                bd.putString("id", data.getStringExtra("result"))
                handoverDiretor.arguments = bd
                UseFragmentManager.displayFragment(showFragment, handoverDiretor,
                        supportFragmentManager, R.id.main_ll)
                showFragment = handoverDiretor
            }
            if (requestCode==QRCODE){

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        showFragment?.onActivityResult(requestCode,resultCode,data)
    }

//    fun setTextView(title: Int): Unit {
//        title_text.setText(title)
//    }

    fun showFragment(initFragment: Fragment): Unit {
        UseFragmentManager.displayFragment(showFragment, initFragment,
                supportFragmentManager, R.id.main_ll)
        fragments.add(showFragment!!)
        showFragment = initFragment
        nowState++
    }


    fun putData(key: String, data: Any): Unit {
        enety.put(key, data)
    }

    fun <E> getData(key: String): E {
        return enety[key] as E
    }

    fun setBar(bar: Bar): Unit {
        if (bar.textBar.equals("")) {
            title_text.visibility = View.GONE
            title_back.visibility = View.VISIBLE
            if (bar.isLeft) bar_back.visibility = View.VISIBLE else bar_back.visibility = View.GONE
            if (bar.isRight) bar_more.visibility = View.VISIBLE else bar_more.visibility = View.GONE
        } else {
            title_back.visibility = View.GONE
            title_text.visibility = View.VISIBLE
            title_text.setText(bar.textBar)
        }
    }


    fun style(function: Bar.() -> Unit): Unit {
        val bar = Bar()
        bar.function()
        setBar(bar)
    }

    class Bar {
        var textBar = ""
        var isLeft = true
        var isRight = true

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (showFragment is HomeFragment || showFragment is ClassFragment || showFragment is MsgFragment || showFragment is MineFragment) {
            if (System.currentTimeMillis() - backPressTime < 1000) {
                finish()
            } else {
                "再按一次返回键退出应用".toast(this)
                backPressTime = System.currentTimeMillis()
            }
        } else {
            backFragment()
        }

    }

    fun backFragment(): Unit {
        if (nowState > 0) {
            UseFragmentManager.displayFragment(showFragment, fragments[nowState - 1],
                    supportFragmentManager, R.id.main_ll)
            showFragment = fragments[nowState - 1]
            nowState--
        } else {
            fragments.clear()
        }
    }

}