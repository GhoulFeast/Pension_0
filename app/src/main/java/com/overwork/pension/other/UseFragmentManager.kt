package com.overwork.pension.other


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log

object UseFragmentManager {
    /**
     * 显示Framgent，做了显示隐藏优化，只在第一次进入时调用onCreateView()方法
     *
     * @param fragments
     * @param fragment
     * @param fm
     * @param containerViewId
     */
    fun displayFragment( from: Fragment?,to: Fragment, fm: FragmentManager,
                        containerViewId: Int) {
        val ft = fm.beginTransaction()
//        if (from!=null){
//            ft.hide(from)
//            ft.remove(from)
////
//        }
//        if (to.isAdded) {
//            ft.show(to)
//            ft.commit()
//        } else {
//            ft.add(containerViewId, to)
//            ft.show(to)
//            ft.commit()
//        }
        if (from==null){
            ft.add(containerViewId, to)
            ft.show(to)
            ft.commitAllowingStateLoss()
        }else{
            ft.replace(containerViewId,to)
            ft.commitAllowingStateLoss()
        }

    }

    /**
     * 隐藏Fragments
     *
     * @param fragments
     * @param ft
     */
    fun hideFragments(fragments: List<Fragment>, ft: FragmentTransaction) {
        for (i in fragments.indices) {
            if (fragments[i] != null) {
                ft.hide(fragments[i])
            }
        }
    }
}
