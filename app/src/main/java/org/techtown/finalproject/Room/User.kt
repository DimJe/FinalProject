package org.techtown.finalproject.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "userNumber") var userNumber : String?,
    @ColumnInfo(name = "passWord") var passWord : String?
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}