package org.techtown.finalproject

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
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
    private lateinit var scheduleRecyclerViewAdapter: RecyclerViewAdapter
    private val map = mapOf(1 to "Jan",2 to "Feb",3 to "Mar",4 to "Apr",5 to "May",6 to "Jun", 7 to "Jul",8 to "Aug",9 to "Sept",10 to "Oct",11 to "Nov",12 to "Dec")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view_with_cal)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        api.data.observe(this, {
            Log.d(TAG, "observe : called ")
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            when {
                api.data.value!!.isEmpty() -> {
                }
                it[0].startMonth==66 -> {
                    dialog.dismiss()
                    Toast.makeText(this,"과제가 없습니다.",Toast.LENGTH_SHORT).show()
                    initView(api.data.value!!)
                    api.data.value!!.clear()
                }
                it[0].startMonth==99 -> {
                    dialog.dismiss()
                    Toast.makeText(this, "학번과 패스워드가 잘못되었습니다", Toast.LENGTH_SHORT).show()
                    api.data.value!!.clear()
                    finish()
                }
                it[0].startMonth==11 -> {
                    dialog.dismiss()
                    Toast.makeText(this, "서버가 꺼졌누....", Toast.LENGTH_SHORT).show()
                    api.data.value!!.clear()
                    finish()
                }
                else -> {
                    dialog.dismiss()
                    initView(api.data.value!!)
                }
            }

        })
        logOut.setOnClickListener {
            Log.d(TAG, "onCreate: logout-clicked")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("로그아웃")
            builder.setMessage("로그아웃을 하시겠습니까?")
            builder.setIcon(R.drawable.logo)

            // 버튼 클릭시에 무슨 작업을 할 것인가!
            val listener = DialogInterface.OnClickListener { _, p1 ->
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE ->{
                        val sharedPreferences = getSharedPreferences("token",MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("auto",false)
                        editor.apply()
                        api.data.value!!.clear()
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE ->{
                    }

                }
            }

            builder.setPositiveButton("네", listener)
            builder.setNegativeButton("아니요", listener)
            builder.create()
            builder.show()
        }

    }
    private fun initView(taskList: ArrayList<Taskinfo>) {

        Log.d(TAG, "initView: called")
        scheduleRecyclerViewAdapter = RecyclerViewAdapter(this,taskList)

        rv_schedule.layoutManager = GridLayoutManager(this, BaseCalendar.DAYS_OF_WEEK)
        rv_schedule.adapter = scheduleRecyclerViewAdapter

        tv_prev_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToPrevMonth()
        }

        tv_next_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToNextMonth()
        }
    }
    fun refreshCurrentMonth(calendar: Calendar) {
        tv_current_month.text = map[calendar.time.month+1]
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그아웃")
        builder.setMessage("로그아웃을 하시겠습니까?")
        builder.setIcon(R.drawable.logo)

        // 버튼 클릭시에 무슨 작업을 할 것인가!
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->{
                    val sharedPreferences = getSharedPreferences("token",MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("auto",false)
                    editor.apply()
                    api.data.value!!.clear()
                    finish()
                }
                DialogInterface.BUTTON_NEGATIVE ->{
                }

            }
        }

        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니요", listener)
        builder.create()
        builder.show()
    }
}
