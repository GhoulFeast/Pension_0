package com.overwork.pension.other

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.overwork.pension.R

/**
 * Created by feima on 2018/9/8.
 */
 object  ImageFull {

     fun showFullWindowViewPage(context: Activity,imageView: ImageView, imageList: List<String>, position: Int): PopupWindow {
        // 将布局文件转换成View对象，popupview 内容视图
        var mPopView = (context as Activity).layoutInflater.inflate(com.aisino.tool.R.layout.image_full_viewpage, null)
        // 将转换的View放置到 新建一个popuwindow对象中
        var mPopupWindow = PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        // 点击popuwindow外让其消失
        mPopupWindow.setOutsideTouchable(true)
        var view = mPopView.findViewById<ViewPager>(com.aisino.tool.R.id.show_full_viewpage)
        view.adapter = ViewPagerAdapter((context as Activity), imageList, mPopupWindow)
        view.currentItem = position
        mPopupWindow.showAsDropDown(imageView)
        return mPopupWindow
    }

    class ViewPagerAdapter(context: Activity, imageList: List<String>, mPopupWindow: PopupWindow) : PagerAdapter() {
        var imageList: List<String> = imageList
        var mPopupWindow = mPopupWindow
        var context = context
        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container!!.removeView(`object` as View);
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            var view = LayoutInflater.from(context).inflate(com.aisino.tool.R.layout.image_full_window, null, false)
            var imageView = view.findViewById<ImageView>(com.aisino.tool.R.id.show_full_window)
            container!!.addView(view)
            Glide.with(context).load(UP_IMAGE + imageList!![position]).error(R.mipmap.picture).into(imageView)
            imageView.setOnClickListener {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
            return view
        }

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return imageList.size
        }

    }
}