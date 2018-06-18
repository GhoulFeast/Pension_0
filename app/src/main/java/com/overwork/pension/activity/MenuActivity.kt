package com.overwork.pension.activity

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DialogTitle
import android.widget.RadioGroup
import com.overwork.pension.R
import com.overwork.pension.fragment.ClassFragment
import com.overwork.pension.fragment.HomeFragment
import com.overwork.pension.fragment.MineFragment
import com.overwork.pension.fragment.MsgFragment
import com.overwork.pension.other.UseFragmentManager
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity :AppCompatActivity(){

    private var showFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
//        var homeFragment = HomeFragment()
//
//        UseFragmentManager.displayFragment(null, homeFragment,
//                supportFragmentManager, R.id.main_ll)
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
    }

    fun setTextView(title: Int): Unit {
        title_text.setText(title)
    }
}