package com.locked.shingranicommunity.authenticate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.locked.shingranicommunity.tutorials.User
import com.locked.shingranicommunity.tutorials.UserDao

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
}