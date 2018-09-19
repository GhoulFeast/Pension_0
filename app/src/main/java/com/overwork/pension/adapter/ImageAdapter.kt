package com.overwork.pension.adapter

import android.app.Activity
import android.content.Context
import android.media.AudioManager.STREAM_MUSIC
import android.media.MediaPlayer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aisino.tool.toast
import com.aisino.tool.widget.showFullWindow
import com.bumptech.glide.Glide
import com.overwork.pension.R
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.fragment.OldInfoFragment
import com.overwork.pension.other.AudioMngHelper
import com.overwork.pension.other.ImageFull
import com.overwork.pension.other.UP_IMAGE
import com.overwork.pension.other.UP_SOUND

class ImageAdapter(fragment: Fragment, activity: Context, taskList: ArrayList<String>, val type: Int) : BaseAdapter() {
    private var list: List<String>? = null
    private var context: Context? = null
    private var fragment = fragment

    init {
        this.list = taskList
        this.context = activity
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_image, null)
        var image = view.findViewById<ImageView>(R.id.show_image)
        if (type == 0) {
            Glide.with(context).load(UP_IMAGE + list!![p0]).error(R.mipmap.picture).into(image)
        }
        image.setOnClickListener {
            try {
                if (type == 0) {
                    if ((fragment as OldInfoFragment).popwindows != null) {
                        if ((fragment as OldInfoFragment).popwindows!!.isShowing) {
                            (fragment as OldInfoFragment).popwindows?.dismiss()
                        }
                    }
                    (fragment as OldInfoFragment).popwindows = ImageFull.showFullWindowViewPage(context as Activity, image, list!!, p0)
//                    image.showFullWindow()
                } else {
                    val aution=AudioMngHelper(menuActivity)
                    aution.setAudioType(AudioMngHelper.TYPE_MUSIC)
                    aution.setVoice100(100)
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(UP_SOUND + list!![p0])
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                    } else {
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "加载文件失败".toast(context!!)
            }

        }
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list?.size!!
    }

}