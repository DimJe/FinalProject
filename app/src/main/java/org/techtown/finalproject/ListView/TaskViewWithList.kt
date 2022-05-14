package org.techtown.finalproject.ListView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_task_view_with_list.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.R

class TaskViewWithList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_list)
        var tasks = intent.getSerializableExtra("data") as ArrayList<Taskinfo>
        val m = intent.getStringExtra("month")
        val d = intent.getStringExtra("day")

        month.text = m
        day.text = d
        tasks.forEach {
            task.text = it.taskName
        }

    }
}