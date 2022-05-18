package org.techtown.finalproject.ListView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_task.view.*
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.ViewHolderHelper
import org.techtown.finalproject.R

class ListViewAdapter(val todayList: ArrayList<Taskinfo>): RecyclerView.Adapter<ViewHolderHelper>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_task, parent, false)
        return ViewHolderHelper(view)
    }

    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
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