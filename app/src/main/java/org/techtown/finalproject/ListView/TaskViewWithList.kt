package org.techtown.finalproject.ListView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_task_view_with_list.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.MainActivity.Companion.TAG
import org.techtown.finalproject.R

class TaskViewWithList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_list)
        var tasks = intent.getSerializableExtra("data") as ArrayList<Taskinfo>
        val m = intent.getStringExtra("month")
        val d = intent.getStringExtra("day")
        tasks.sortWith(compareBy<Taskinfo>{it.endMonth}.thenBy { it.endDay })
        tasks.forEach {
            Log.d(TAG, "onCreate: ${it.endMonth},${it.endDay}")
        }
        monthDay.text = m + "월 " + d + "일의 할 일"
    }
}