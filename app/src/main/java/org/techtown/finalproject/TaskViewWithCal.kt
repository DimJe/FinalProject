package org.techtown.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_task_view_with_cal.*
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_cal)

        api.data.observe(this, Observer{
            Log.d(TAG, "observe : called ")
            initView(api.data.value!!)
        })

    }
    fun initView(taskList: ArrayList<Taskinfo>) {

        scheduleRecyclerViewAdapter = RecyclerViewAdapter(this,taskList)

        rv_schedule.layoutManager = GridLayoutManager(this, BaseCalendar.DAYS_OF_WEEK)
        rv_schedule.adapter = scheduleRecyclerViewAdapter
        rv_schedule.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        rv_schedule.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

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