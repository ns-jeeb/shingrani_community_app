package com.locked.shingranicommunity.tutorials

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "user")
data class User(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "username")var username: String,
    @ColumnInfo(name = "name")var name: String ,
    @ColumnInfo(name = "publicCreation")var publicCreation: Boolean,
    @ColumnInfo(name = "administrator")var administrator: Boolean) {

}

data class RegisterUser(val username: String,val password: String,val name:String ){

}