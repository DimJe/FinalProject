package org.techtown.finalproject.APIViewModel

class Taskinfo {
    var startDay : Int = 0
    var startMonth : Int = 0
    var endDay : Int = 0
    var endMonth : Int = 0
    var taskName : String = ""
    var taskLine : Int = 0
    constructor(startDate : String,endDate : String,taskName : String){
        this.startDay = (startDate[8].toString()+startDate[9].toString()).toInt()
        this.startMonth = (startDate[5].toString()+startDate[6].toString()).toInt()
        this.endDay = (endDate[8].toString()+endDate[9].toString()).toInt()
        this.endMonth = (endDate[5].toString()+endDate[6].toString()).toInt()
        this.taskName = taskName
    }
}