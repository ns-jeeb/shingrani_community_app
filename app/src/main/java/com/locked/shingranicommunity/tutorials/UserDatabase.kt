package com.locked.shingranicommunity.tutorials

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun loginDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        private var LOCk = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCk) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            UserDatabase::class.java, "login-user-list.db"
        ).build()
    }
}