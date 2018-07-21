package com.overwork.pension.adapter

import android.content.Context
import android.media.MediaPlayer
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.aisino.tool.widget.showFullWindow
import com.bumptech.glide.Glide
import com.overwork.pension.R

class ImageAdapter(activity: Context, taskList: ArrayList<String>, val type: Int) : BaseAdapter() {
    private var list: List<String>? = null
    private var context: Context? = null

    init {
        this.list = taskList
        this.context = activity
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_image, null)
        var image = view.findViewById<ImageView>(R.id.show_image)
        if (type==0){
            Glide.with(context).load(list!![p0]).into(image)
        }
        image.setOnClickListener {
            if (type == 0) {
                image.showFullWindow()
            } else {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.reset()
                mediaPlayer.setDataSource(list!![p0])
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                } else {
                    mediaPlayer.prepare()
                }
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