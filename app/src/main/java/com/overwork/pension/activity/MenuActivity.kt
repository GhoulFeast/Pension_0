package com.overwork.pension.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.overwork.pension.R
import com.overwork.pension.fragment.ClassFragment
import com.overwork.pension.fragment.HomeFragment
import com.overwork.pension.fragment.MineFragment
import com.overwork.pension.fragment.MsgFragment
import com.overwork.pension.other.UseFragmentManager
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity :AppCompatActivity(){

    private var showFragment: Fragment? = null
    private val enety:MutableMap<String,Any> = mutableMapOf()
    private val fragments=ArrayList<Fragment>()
    private var nowState=0;




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
                    showFragment=homeFragment
                }
                R.id.main_rb_class -> {
                    var classFragment= ClassFragment()
                    UseFragmentManager.displayFragment(showFragment, classFragment,
                            supportFragmentManager, R.id.main_ll)
                    showFragment=classFragment

                }
                R.id.main_rb_msg -> {
                    var msgFragment=MsgFragment()
                    UseFragmentManager.displayFragment(showFragment, msgFragment,
                            supportFragmentManager, R.id.main_ll)
                    showFragment=msgFragment

                }
                R.id.main_rb_mine ->{
                    var mineFragment=MineFragment()
                    UseFragmentManager.displayFragment(showFragment, mineFragment,
                            supportFragmentManager, R.id.main_ll)
                    showFragment=mineFragment

                }
            }
        })
        main_rb_homepage.performClick()
        title_back.setOnClickListener{
            backFragment()
        }
    }

    fun setTextView(title: Int): Unit {
        title_text.setText(title)
    }

    fun showFragment(initFragment: Fragment): Unit {
        UseFragmentManager.displayFragment(showFragment, initFragment,
                supportFragmentManager, R.id.main_ll)
        showFragment=initFragment
        fragments.add(initFragment)
        nowState++
    }

    fun putData(key:String,data:Any): Unit {
        enety.put(key,data)
    }

    fun <E>getData(key:String): E {
       return enety[key] as E
    }

    fun setBar(bar:Bar): Unit {
        if (bar.textBar==""){
            title_text.visibility= View.GONE
            if (bar.isLeft)bar_back.visibility= View.VISIBLE else bar_back.visibility= View.GONE
            if (bar.isRight)bar_more.visibility= View.VISIBLE else bar_more.visibility= View.GONE
        }else{
            title_back.visibility= View.GONE
        }
    }


    fun style(function: Bar.() -> Unit): Unit {
        val bar=Bar()
        bar.function()
        setBar(bar)
    }

    class Bar{
        var textBar=""
        var isLeft=true
        var isRight=true

    }

    override fun onBackPressed() {
//        super.onBackPressed()
       if (showFragment is HomeFragment||showFragment is ClassFragment||showFragment is MsgFragment||showFragment is MineFragment){
           finish()
       }else{
           backFragment()
       }

    }

    fun backFragment(): Unit {
        if (nowState>0){
            showFragment(fragments[nowState-1])
            nowState--
        }else{

        }
    }

}