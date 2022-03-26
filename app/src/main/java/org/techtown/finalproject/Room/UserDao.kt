package org.techtown.finalproject.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {

    @Insert
    fun insert(user:User)

    @Query("SELECT * FROM user")
    fun get() : User

    @Query("DELETE FROM user")
    fun clear()

    @Query("SELECT COUNT(*) FROM user")
    fun check() : Int
}