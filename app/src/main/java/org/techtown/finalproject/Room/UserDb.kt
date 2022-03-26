package org.techtown.finalproject.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)

abstract class UserDb: RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        private var instance: UserDb? = null

        @Synchronized
        fun getInstance(context: Context): UserDb? {
            if (instance == null) {
                synchronized(UserDb::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDb::class.java,
                        "user-data"
                    )
                        .build()
                }
            }
            return instance
        }
    }
}