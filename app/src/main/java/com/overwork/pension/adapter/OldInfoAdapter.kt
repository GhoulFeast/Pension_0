package com.overwork.pension.adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aisino.tool.log
import com.aisino.tool.widget.ToastAdd
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity

class OldInfoAdapter(val context: FragmentActivity, val list: ArrayList<MutableMap<String, Any>>):BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_oldinfo_abnormal, null)
        var room = view.findViewById<TextView>(R.id.item_old_info_room)
        var times = view.findViewById<TextView>(R.id.item_old_info_times)
        var title = view.findViewById<TextView>(R.id.item_old_info_title)
        var submit_time = view.findViewById<TextView>(R.id.item_old_info_submit_time)
        var submit_name = view.findViewById<TextView>(R.id.item_old_info_submit_name)
        var icontent = view.findViewById<TextView>(R.id.item_old_info_content)
        var image_gv = view.findViewById<GridView>(R.id.item_old_info_image_gv)
        var sound_gv = view.findViewById<GridView>(R.id.item_old_info_sound_gv)
        room.setText(list[p0]["fjh"].toString())
        times.setText(list[p0]["kssj"].toString()+"-"+list[p0]["jssj"].toString())
        title.setText(list[p0]["abnormalTitle"].toString() )
        submit_time.setText(list[p0]["submitTime"].toString() )
        submit_name.setText(list[p0]["submitName"].toString() )
        icontent.setText(list[p0]["abnormalContent"].toString() )
        var img= list[p0]["imageUrl"].toString()
        val images=ArrayList<String>()
        while (img.indexOf(",")>0){
            images.add(img.substring(0, img.indexOf(",")))
            img=img.substring(img.indexOf(",")+1,img.length )
        }
        images.add(img)
        var sound= list[p0]["imageUrl"].toString()
        val sounds=ArrayList<String>()
        while (sound.indexOf(",")>0){
            sounds.add(sound.substring(0, sound.indexOf(",")))
            sound=sound.substring(sound.indexOf(",")+1,sound.length )
        }
        sounds.add(sound)
        image_gv.adapter=ImageAdapter(context,images,0)
        sound_gv.adapter=ImageAdapter(context,sounds,1)
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