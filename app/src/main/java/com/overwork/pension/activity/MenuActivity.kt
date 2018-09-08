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
import android.widget.PopupWindow
import android.widget.RadioButton
import com.aisino.qrcode.activity.CaptureActivity
import com.aisino.tool.log
import com.aisino.tool.toast
import com.aisino.tool.widget.openUnterTheViewListWindow
import com.overwork.pension.R
import com.overwork.pension.fragment.*
import com.overwork.pension.other.UseFragmentManager
import com.overwork.pension.other.userType
import com.overwork.pension.service.AutoUpdateService
import kotlinx.android.synthetic.main.activity_menu.*

val QRCODE = 999

lateinit var menuActivity: MenuActivity

class MenuActivity : AppCompatActivity(), ServiceConnection {


    var showFragment: Fragment? = null
    private val enety: MutableMap<String, Any> = mutableMapOf()
    private val fragments = ArrayList<Fragment>()
    private var backPressTime = 0L
    private var auBinder: AutoUpdateService.Binder? = null
    var popupWindow: PopupWindow? = null

    lateinit var selectRadio: RadioButton
    private var selectId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        menuActivity = this
        initViewAndEvent()
    }

    fun initViewAndEvent(): Unit {

//        main_rg.setOnCheckedChangeListener({ radioGroup, i ->
//            when (i) {
        main_rb_homepage.setOnClickListener {
            fragments.clear()
            var homeFragment = HomeFragment()
            UseFragmentManager.displayFragment(showFragment, homeFragment,
                    supportFragmentManager, R.id.main_ll)
            showFragment = homeFragment
            main_rb_homepage.setRadioTopBitmp(R.mipmap.task_s)
            selectRadio = main_rb_homepage
            selectId = R.mipmap.task
            auBinder?.setRun(true)
        }
        main_rb_class.setOnClickListener {
            fragments.clear()
            if (userType.toInt() == 2) {
                var classFragment = HandoverDirectorFragment()
                isZJ = true
//                        startActivityForResult(Intent(this, CaptureActivity::class.java), -1)
                UseFragmentManager.displayFragment(showFragment, classFragment,
                        supportFragmentManager, R.id.main_ll)
                showFragment = classFragment
                main_rb_class.setRadioTopBitmp(R.mipmap.jjb_s)
                selectRadio = main_rb_class
                selectId = R.mipmap.jjb
            } else {
                var classFragment = HandoverInfoFragment()
                UseFragmentManager.displayFragment(showFragment, classFragment,
                        supportFragmentManager, R.id.main_ll)
                showFragment = classFragment
                main_rb_class.setRadioTopBitmp(R.mipmap.jjb_s)
                selectRadio = main_rb_class
                selectId = R.mipmap.jjb
            }

            auBinder?.setRun(true)

        }
        main_rb_msg.setOnClickListener {
            fragments.clear()
            var msgFragment = MsgFragment()
            UseFragmentManager.displayFragment(showFragment, msgFragment,
                    supportFragmentManager, R.id.main_ll)
            showFragment = msgFragment
            main_rb_msg.setRadioTopBitmp(R.mipmap.msg_s)
            selectRadio = main_rb_msg
            selectId = R.mipmap.msg
            auBinder?.setRun(false)
        }
        main_rb_mine.setOnClickListener {
            fragments.clear()
            var mineFragment = MineFragment()
            UseFragmentManager.displayFragment(showFragment, mineFragment,
                    supportFragmentManager, R.id.main_ll)
            showFragment = mineFragment
            main_rb_mine.setRadioTopBitmp(R.mipmap.mine_s)
            selectRadio = main_rb_mine
            selectId = R.mipmap.mine
            auBinder?.setRun(true)
        }
//            }

//        })
        main_rb_homepage.performClick()
        bar_back.setOnClickListener {
            backFragment()
        }
        bar_more.setOnClickListener {
            popupWindow = openUnterTheViewListWindow(it, ArrayList<String>().apply { add("扫一扫") }, {
                startActivityForResult(Intent(this@MenuActivity, CaptureActivity::class.java), QRCODE)
            })
        }
        val intent = Intent(this@MenuActivity, AutoUpdateService::class.java)
        bindService(intent, this@MenuActivity, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    fun RadioButton.setRadioTopBitmp(resid: Int): Unit {
        var dra = resources.getDrawable(resid)
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight())
        this.setCompoundDrawables(main_rb_msg.getCompoundDrawables()[0], dra, main_rb_msg.getCompoundDrawables()[2], main_rb_msg.getCompoundDrawables()[3])
        if (selectId != 0) {
            var rdra = resources.getDrawable(selectId)
            rdra.setBounds(0, 0, rdra.getMinimumWidth(), rdra.getMinimumHeight())
            selectRadio.setCompoundDrawables(main_rb_msg.getCompoundDrawables()[0], rdra, main_rb_msg.getCompoundDrawables()[2], main_rb_msg.getCompoundDrawables()[3])
        }
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
            override fun setMsgNum(num: Boolean) {
                runOnUiThread {
                    if (num) {
                        var dra = resources.getDrawable(R.mipmap.msg_red)
                        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight())
                        main_rb_msg.setCompoundDrawables(main_rb_msg.getCompoundDrawables()[0], dra, main_rb_msg.getCompoundDrawables()[2], main_rb_msg.getCompoundDrawables()[3])
                    } else {
                        var dra = resources.getDrawable(R.mipmap.msg)
                        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
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
//            if (requestCode == -1 && resultCode == Activity.RESULT_OK) {
//                var handoverDiretor = HandoverDirectorFragment()
//                var bd = Bundle()
//                bd.putString("id", data.getStringExtra("result"))
//                handoverDiretor.arguments = bd
//                UseFragmentManager.displayFragment(showFragment, handoverDiretor,
//                        supportFragmentManager, R.id.main_ll)
//                showFragment = handoverDiretor
//            }
            if (requestCode == QRCODE) {//二维码处理
                var qr = data.getStringExtra("result")
                val code = qr.substring(4, qr.length)
                qr.log("code")
                var qrFrgment: Fragment? = null
                when (qr.substring(0, 1)) {
                    "F" -> {
                        putData("fjpkid", code)
                        qrFrgment = RoomListFragment()
                        showFragment(qrFrgment)
//                        UseFragmentManager.displayFragment(showFragment, qrFrgment!!,
//                                supportFragmentManager, R.id.main_ll)
//                        showFragment = qrFrgment
                    }
                    "C" -> {
                        putData("cwpkid", code)
                        qrFrgment = TodayTaskFragment()
//                        UseFragmentManager.displayFragment(showFragment, qrFrgment!!,
//                                supportFragmentManager, R.id.main_ll)
//                        showFragment = qrFrgment
                        showFragment(qrFrgment)
                    }
                    "Z" -> {
                        if (userType.equals("2")) {
                            isZJ = true
                            putData("jbrid", code)
                            main_rb_class.performClick()
//                            qrFrgment = HandoverDirectorFragment()
//                            UseFragmentManager.displayFragment(showFragment, qrFrgment!!,
//                                    supportFragmentManager, R.id.main_ll)
//                            showFragment = qrFrgment
                        } else {
                            "只有主管才能交接班".toast(this)
                        }
                    }
                    else -> {
                        "不是支持的二维码格式".toast(this)
                    }
                }
//
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        showFragment?.onActivityResult(requestCode, resultCode, data)
    }

//    fun setTextView(title: Int): Unit {
//        title_text.setText(title)
//    }

    fun showFragment(initFragment: Fragment): Unit {
        UseFragmentManager.displayFragment(showFragment, initFragment,
                supportFragmentManager, R.id.main_ll)
        fragments.add(showFragment!!)
        showFragment = initFragment
    }


    fun putData(key: String, data: Any): Unit {
        enety.put(key, data)
    }

    fun <E> getData(key: String): E {
        return enety[key] as E
    }

    fun removeData(key: String): Unit {
        enety.remove(key)
    }

    fun hasData(key: String): Boolean {
        return enety.contains(key)
    }

    fun setBar(bar: Bar): Unit {
        if (bar.textBar.equals("")) {
            title_text.visibility = View.GONE
//            title_back.visibility = View.VISIBLE
            bar_title.text = bar.titleBar
            if (bar.isLeft) bar_back.visibility = View.VISIBLE else bar_back.visibility = View.GONE
//            if (bar.isRight) bar_more.visibility = View.VISIBLE else bar_more.visibility = View.GONE
        } else {
            bar_back.visibility = View.GONE
//            title_back.visibility = View.GONE
            title_text.visibility = View.VISIBLE
            title_text.setText(bar.textBar)
            bar_title.text = ""
        }
        bar.textBar.log()
    }


    fun style(function: Bar.() -> Unit): Unit {
        val bar = Bar()
        bar.function()
        setBar(bar)
    }

    class Bar {
        var textBar = ""
        var isLeft = true
        //        var isRight = true
        var titleBar = ""

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (showFragment is TaskDetailsFragment){//护理详情图片消除
            if((showFragment as TaskDetailsFragment).imgPopupWindow!=null){
                if ((showFragment as TaskDetailsFragment).imgPopupWindow!!.isShowing){
                    (showFragment as TaskDetailsFragment).imgPopupWindow?.dismiss()
                    return
                }
            }
        }
        if (showFragment is OldInfoFragment){//护理详情图片消除
            if((showFragment as OldInfoFragment).popwindows!=null){
                if ((showFragment as OldInfoFragment).popwindows!!.isShowing){
                    (showFragment as OldInfoFragment).popwindows?.dismiss()
                    return
                }
            }
        }



        if (popupWindow != null) {
            popupWindow?.dismiss()
        }
        if (showFragment is HomeFragment || showFragment is HandoverInfoFragment || showFragment is MsgFragment || showFragment is MineFragment||showFragment is HandoverEndFragment||showFragment is HandoverDirectorFragment) {
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
        if (fragments.size > 0) {
            UseFragmentManager.displayFragment(showFragment, fragments[fragments.size - 1],
                    supportFragmentManager, R.id.main_ll)
            showFragment = fragments[fragments.size - 1]
            fragments.removeAt(fragments.size - 1)
            if (showFragment is HomeFragment) {
                removeData("RoomList")
                removeData("cwpkid")
                removeData(lrId)
            }
        } else {
            fragments.clear()
        }
    }

}