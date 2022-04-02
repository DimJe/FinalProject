package org.techtown.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.activity_task_view_with_cal.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.AdapterMonth

class TaskViewWithCal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_cal)

        val taskList : ArrayList<Taskinfo> = ArrayList()
        taskList.add(Taskinfo("2022-04-01 00:00","2022-04-05 23:59","[일반 과제]1st report"))
        taskList.add(Taskinfo("2022-04-02 00:00","2022-04-05 23:59","[일반 과제]2st report"))
        taskList.add(Taskinfo("2022-04-03 00:00","2022-04-05 23:59","[일반 과제]3st report"))
        taskList.sortedWith(compareBy<Taskinfo>{it.startMonth}.thenBy {it.startDay})
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth(taskList)

        calendar_custom.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(calendar_custom)
    }
}