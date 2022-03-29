package org.techtown.finalproject.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo(name = "userNumber") val userNumber : String,
    @ColumnInfo(name = "passWord") val passWord : String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}