package org.techtown.finalproject.Calendar

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_day.view.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.MainActivity.Companion.TAG
import org.techtown.finalproject.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>,val taskList : ArrayList<Taskinfo>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    val current = LocalDateTime.now()
    val forMatter = DateTimeFormatter.ofPattern("dd")
    val forMonth = DateTimeFormatter.ofPattern("MM")
    //val forMonthed = current.format(forMonth)
    val forMatted = current.format(forMatter)
    val scheduleList = arrayOfNulls<View>(6)
    val schedule = MutableList<Boolean>(6, init = {false})
    //var scheduleLine : Int = 0
    inner class DayView(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        //Log.d(TAG, "onCreateViewHolder: $tempMonth, ${dayList[15].month}")
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_day, parent, false)
        return DayView(view)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        //Log.d(TAG, "onBindViewHolder: $position")
        scheduleList[0] = holder.layout.one
        scheduleList[1] = holder.layout.two
        scheduleList[2] = holder.layout.three
        scheduleList[3] = holder.layout.four
        scheduleList[4] = holder.layout.five
        scheduleList[5] = holder.layout.six


        //위에서부터 비어있는 라인을 배정해줄 로직이 필요함....
        // 색이 겹치면 같은 과제라고 착각 할 수 있으니 색은 랜덤으로 투명도만 낮춰서 배정하는게 좋을듯함
        taskList.forEachIndexed { index, it ->
            if (it.startMonth-1==dayList[position].month && it.startDay==dayList[position].date){
//                schedule[scheduleLine] = true
//                scheduleLine++
                for(i in 0..5){
                    if(!schedule[i]){
                        schedule[i] = true
                        it.taskLine = i
                        break
                    }
                }
            }
        }

        for(i in 0..5){
            if(schedule[i]){
                scheduleList[i]!!.visibility = VISIBLE
            }
        }

        taskList.forEachIndexed { index, it ->
            if (it.endMonth-1==dayList[position].month && it.endDay==dayList[position].date){
                schedule[it.taskLine] = false
            }
        }

        holder.layout.item_day_layout.setOnClickListener {
            Toast.makeText(holder.layout.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
        }
        holder.layout.item_day_text.text = dayList[position].date.toString()

        holder.layout.item_day_text.setTextColor(when(position % 7) {
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })

        if(tempMonth != dayList[position].month - 1) {
            holder.layout.item_day_text.alpha = 0.4f
            holder.layout.item_day_text.setTypeface(null, Typeface.NORMAL)
        }

    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}