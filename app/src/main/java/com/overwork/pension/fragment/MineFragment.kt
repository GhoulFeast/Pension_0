package com.overwork.pension.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aisino.tool.ani.LoadingDialog
import com.bumptech.glide.Glide
import com.hq.kbase.network.Http
import com.overwork.pension.MainActivity
import com.overwork.pension.R
import com.overwork.pension.activity.MenuActivity
import com.overwork.pension.activity.menuActivity
import com.overwork.pension.adapter.HandoverInfoAdapter
import com.overwork.pension.adapter.TomorrowTaskAdapter
import com.overwork.pension.other.*
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_today_task.*

class MineFragment : Fragment() {
    lateinit var tomorrowTaskAdp: TomorrowTaskAdapter
    var tomorrowTasks: ArrayList<MutableMap<String, Any>> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mine, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tomorrowTaskAdp = TomorrowTaskAdapter(activity, tomorrowTasks)
        mine_task_list.adapter = tomorrowTaskAdp
        mine_user_name.setText(userName)
        mine_user_job.setText(userLevelName)
        mine_user_starttime.setText(String.format(resources.getString(R.string.text_entryTime, entryTime)))
        mine_user_overtime.setText(String.format(resources.getString(R.string.text_workingyears, workingYears)))
        mine_user_ld.setText(String.format(resources.getString(R.string.text_superiorName, superiorName)))
        Glide.with(activity).load(UP_HEAD + userPortrait).error(R.mipmap.hs).bitmapTransform(CropCircleTransformation(activity)).into(mine_user_icon)
        tomorrowTaskAdp.setTomorrow(object : TomorrowTaskAdapter.OnTomorrow {
            override fun OnHandoverClick(id: String) {
                var old = OldInfoFragment()
                var bd = Bundle();
                bd.putString("id", id)
                old.arguments = bd
                (activity as MenuActivity).showFragment(old)
            }
        })

        mine_exit_user.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("exit", true)
            startActivity(intent)
            activity.finish()
        }
        mine_checkupdata.setOnClickListener {
            menuActivity.checkUpdata()
        }

        getData()
        (activity as MenuActivity).style {
            textBar = activity.resources.getString(R.string.ylyxt)
        }
        getVersionNum();
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        (activity as MenuActivity).style {
            textBar = activity.resources.getString(R.string.ylyxt)
        }
    }

    fun getData() {
        val dialog = LoadingDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http.post {
            url = BASEURL + TOMORROW_TASK
            "userId" - userId
            success {
                menuActivity.runOnUiThread {
                    if ((!"status").equals("200")) {
                        tomorrowTasks.clear()
                        var tomorrows: ArrayList<MutableMap<String, Any>> = "result".."tomorrow"
                        tomorrows.let { tomorrowTasks.addAll(tomorrows) }
                        tomorrowTaskAdp.notifyDataSetChanged()
                    }
                    dialog.dismiss()
                }
            }
            fail {
                dialog.dismiss()
            }
        }
    }

    fun getVersionNum() {
        Http.post {
            url = BASEURL + VERSION_NUM
            success {
                activity.runOnUiThread {
                    if ((!"status").toInt() == 200) {
                        mine_checkupdata.setText("检查更新，最新版本" + (!"result"))
                    }
                }

            }
            fail {

            }
        }

    }
}