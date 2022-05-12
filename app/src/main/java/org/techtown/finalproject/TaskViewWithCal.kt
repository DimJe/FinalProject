package org.techtown.finalproject

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_task_view_with_cal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.BaseCalendar
import org.techtown.finalproject.Calendar.RecyclerViewAdapter
import org.techtown.finalproject.MainActivity.Companion.TAG
import org.techtown.finalproject.MainActivity.Companion.api
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskViewWithCal : AppCompatActivity() {
    lateinit var scheduleRecyclerViewAdapter: RecyclerViewAdapter
    //val loading = LoadingDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_cal)

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        api.data.observe(this, Observer{
            Log.d(TAG, "observe : called ")
            if(api.data.value!!.isEmpty()){
                Toast.makeText(this,"몰루",Toast.LENGTH_SHORT).show()

            }
            else if(it[0].startMonth==66){
                dialog.dismiss()
                Toast.makeText(this,"과제가 없습니다.",Toast.LENGTH_SHORT).show()
                initView(api.data.value!!)
            }
            else if(it[0].startMonth==99){
                dialog.dismiss()
                Toast.makeText(this, "학번과 패스워드가 잘못되었습니다", Toast.LENGTH_SHORT).show()
                api.data.value!!.clear()
                finish()
            }
            else{
                Toast.makeText(this,"몰루22",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                initView(api.data.value!!)
            }

        })

    }
    fun initView(taskList: ArrayList<Taskinfo>) {

        Log.d(TAG, "initView: called")
        scheduleRecyclerViewAdapter = RecyclerViewAdapter(this,taskList)

        rv_schedule.layoutManager = GridLayoutManager(this, BaseCalendar.DAYS_OF_WEEK)
        rv_schedule.adapter = scheduleRecyclerViewAdapter
        //rv_schedule.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        //rv_schedule.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        tv_prev_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToPrevMonth()
        }

        tv_next_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToNextMonth()
        }
    }
    fun refreshCurrentMonth(calendar: Calendar) {
        val sdf = SimpleDateFormat("yyyy MM", Locale.KOREAN)
        tv_current_month.text = sdf.format(calendar.time)
    }
}