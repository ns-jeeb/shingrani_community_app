package com.locked.shingranicommunity.tutorials

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "user")
data class User(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "username")var username: String,
    @ColumnInfo(name = "token")var token: String,
    @ColumnInfo(name = "name")var name: String ) {

}