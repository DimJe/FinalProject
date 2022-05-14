package org.techtown.finalproject.APIViewModel

class lmsItem {
    val lms_id : String
    val lms_pw : String
    val token : String
    constructor(id : String,pw : String,token:String){
        this.lms_id = id
        this.lms_pw = pw
        this.token = token
    }
}