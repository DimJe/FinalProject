package org.techtown.finalproject.APIViewModel

import java.io.Serializable

class Taskinfo(
    startDate: String,
    endDate: String,
    var taskName: String,
    var course: String,
    var content: String,
    var professor: String
) :Serializable{
    var startDay : Int = 0
    var startMonth : Int = 0
    var startYear : Int = 0
    var endDay : Int = 0
    var endMonth : Int = 0
    var taskLine : Int = 0

    init {
        this.startDay = (startDate[8].toString()+startDate[9].toString()).toInt()
        this.startMonth = (startDate[5].toString()+startDate[6].toString()).toInt()
        this.endDay = (endDate[8].toString()+endDate[9].toString()).toInt()
        this.endMonth = (endDate[5].toString()+endDate[6].toString()).toInt()
        this.startYear = (startDate[0].toString()+startDate[1].toString()+startDate[2].toString()+startDate[3].toString()).toInt()
    }
}