package org.techtown.finalproject.Calendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_day.view.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.MainActivity.Companion.scheduleList
import org.techtown.finalproject.R
import org.techtown.finalproject.TaskViewWithCal
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by WoochanLee on 22/03/2019.
 */
class RecyclerViewAdapter(val mainActivity: TaskViewWithCal, var taskList: ArrayList<Taskinfo>) : RecyclerView.Adapter<ViewHolderHelper>() {

    val baseCalendar = BaseCalendar()
    val schedule = MutableList<ScheduleItem>(6, init = {ScheduleItem(false,"","","")})
    init {
        baseCalendar.initBaseCalendar {
            refreshView(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_day, parent, false)
        return ViewHolderHelper(view)
    }

    override fun getItemCount(): Int {
        return BaseCalendar.LOW_OF_CALENDAR * BaseCalendar.DAYS_OF_WEEK
    }

    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
        scheduleList[0] = holder.itemView.one
        scheduleList[1] = holder.itemView.two
        scheduleList[2] = holder.itemView.three
        scheduleList[3] = holder.itemView.four
        scheduleList[4] = holder.itemView.five
        scheduleList[5] = holder.itemView.six

        scheduleList.forEach {
            it!!.visibility = View.INVISIBLE
        }

        val dayRange : String = baseCalendar.month.toString()+
                if(baseCalendar.data[position].toString().length==1) "0"+ baseCalendar.data[position].toString() else baseCalendar.data[position].toString()

        Log.i("태그", "onBindViewHolder:  ${baseCalendar.data[position]} ${baseCalendar.month}")
        when {
            position % BaseCalendar.DAYS_OF_WEEK == 0 -> holder.itemView.item_day_text.setTextColor(Color.parseColor("#ff1200"))
            position % BaseCalendar.DAYS_OF_WEEK == 6 -> holder.itemView.item_day_text.setTextColor(Color.BLUE)
            else -> holder.itemView.item_day_text.setTextColor(Color.parseColor("#676d6e"))
        }

        if (position < baseCalendar.prevMonthTailOffset || position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate) {
            //holder.itemView.item_day_text.alpha = 0.3f
            holder.itemView.item_day_layout.visibility = View.INVISIBLE
        } else {
            holder.itemView.item_day_layout.visibility = View.VISIBLE
            holder.itemView.item_day_text.alpha = 1f
        }
        holder.itemView.item_day_text.text = baseCalendar.data[position].toString()

        taskList.forEach {
            if(it.startMonth-1==baseCalendar.month && it.startDay==baseCalendar.data[position]){
                Log.i("태그", "과제 매칭")
                for(i in 0..5){
                    if(!schedule[i].check){
                        schedule[i].check = true
                        schedule[i].startRange = (it.startMonth-1).toString() + if(it.startDay.toString().length==1) "0"+it.startDay.toString() else it.startDay
                        schedule[i].endRange = (it.endMonth-1).toString() + if(it.endDay.toString().length==1) "0"+it.endDay.toString() else it.endDay
                        it.taskLine = i
                        schedule[i].title = it.taskName
                        break
                    }
                }
            }
        }
        for(i in 0..5){
            if(schedule[i].check && (schedule[i].startRange <= dayRange) && (dayRange <= schedule[i].endRange)){
                Log.i("태그", "onBindViewHolder:왜 그려지는거야 싀발 $i ")
                //Log.d(TAG, "schedule : $i")
                scheduleList[i]!!.visibility = View.VISIBLE
                if(schedule[i].title.length>8) {
                    scheduleList[i]!!.text = schedule[i].title.substring(0 until 8)
                    schedule[i].title = schedule[i].title.substring(8)
                }
                else {
                    scheduleList[i]!!.text = schedule[i].title.substring(0)
                    schedule[i].title = ""
                }
            }
        }
        taskList.forEach {
            if (it.endMonth-1==baseCalendar.month && it.endDay==baseCalendar.data[position]){
                Log.i("태그", "과제 마감")
                schedule[it.taskLine].check = false
                schedule[it.taskLine].startRange = ""
                schedule[it.taskLine].endRange = ""
                schedule[it.taskLine].title = ""
                //it.taskLine = -1
            }
        }

    }

    fun changeToPrevMonth() {
        schedule.forEach {
            it.check = false
            it.startRange = ""
            it.endRange = ""
        }
        baseCalendar.changeToPrevMonth {
            refreshView(it)
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            refreshView(it)
        }
    }

    private fun refreshView(calendar: Calendar) {
        notifyDataSetChanged()
        mainActivity.refreshCurrentMonth(calendar)
    }
}