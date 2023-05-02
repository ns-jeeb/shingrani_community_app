package com.locked.shingranicommunity.locked.models

data class User(
    val _id: String,
    val name: String,
    var hideNumber: Boolean,
    val privilege: Privilege,
    val username: String
)