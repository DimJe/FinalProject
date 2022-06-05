package org.techtown.finalproject.ListView

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_task.view.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.ViewHolderHelper
import org.techtown.finalproject.R
import java.util.*
import kotlin.collections.ArrayList

class ListViewAdapter(private val todayList: ArrayList<Taskinfo>, val context:Context): RecyclerView.Adapter<ViewHolderHelper>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_task, parent, false)
        return ViewHolderHelper(view)
    }

    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {

        val date = Calendar.getInstance()
        date.time = Date()
        if(date.time.month+1==todayList[position].endMonth){
            if(todayList[position].endDay-date.time.date<=3){
                holder.itemView.list_item_task.setBackgroundColor(Color.argb(50,255,0,0))
            }
            else if(todayList[position].endDay-date.time.date<=5){
                holder.itemView.list_item_task.setBackgroundColor(Color.argb(50,255,235,59))
            }
        }
        holder.itemView.body.animation = AnimationUtils.loadAnimation(context,R.anim.itemanim)
        holder.itemView.courseName.text = todayList[position].course
        holder.itemView.professor.text = todayList[position].professor
        val str = todayList[position].startMonth.toString() +"-"+todayList[position].startDay + " ~ " +todayList[position].endMonth +"-"+todayList[position].endDay
        holder.itemView.deadLine.text = str
        holder.itemView.taskName.text = todayList[position].taskName
        holder.itemView.taskContent.text = todayList[position].content
        holder.itemView.showMore.setOnClickListener {
            if(holder.itemView.showMore.tag=="0"){
                holder.itemView.showMore.tag = "1"
                holder.itemView.taskContent.visibility = View.VISIBLE
                holder.itemView.showMore.setImageResource(R.drawable.less)
            }else if(holder.itemView.showMore.tag=="1"){
                holder.itemView.showMore.tag="0"
                holder.itemView.taskContent.visibility = View.GONE
                holder.itemView.showMore.setImageResource(R.drawable.more)
            }
        }
    }

    override fun getItemCount(): Int {
        return todayList.size
    }
}