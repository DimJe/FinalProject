package org.techtown.finalproject.ListView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_task_view_with_cal.*
import kotlinx.android.synthetic.main.activity_task_view_with_list.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.MainActivity.Companion.TAG
import org.techtown.finalproject.R

class TaskViewWithList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_list)
        overridePendingTransition(R.anim.enter,R.anim.none)
        var tasks = intent.getSerializableExtra("data") as ArrayList<Taskinfo>
        val m = intent.getStringExtra("month")
        val d = intent.getStringExtra("day")
        tasks.sortWith(compareBy<Taskinfo>{it.endMonth}.thenBy { it.endDay })
        monthDay.text = "ToDo List at " +m+"."+d
        taskRecycler.apply {

            this.layoutManager = LinearLayoutManager(this@TaskViewWithList,LinearLayoutManager.VERTICAL,false)
            this.adapter = ListViewAdapter(tasks,context)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(isFinishing){
            overridePendingTransition(R.anim.none,R.anim.exit)
        }
    }
}