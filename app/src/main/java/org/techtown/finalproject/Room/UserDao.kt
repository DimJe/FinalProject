package org.techtown.finalproject.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query


@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    fun insert(user:User)

    @Query("SELECT * FROM user")
    fun get() : List<User>

    @Query("DELETE FROM user")
    fun delete()

    @Query("SELECT COUNT(*) FROM user")
    fun check() : Int
}