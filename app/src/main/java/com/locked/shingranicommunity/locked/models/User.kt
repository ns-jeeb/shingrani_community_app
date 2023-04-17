package com.locked.shingranicommunity.locked.models

data class User(
    val _id: String,
    val name: String,
    val privilege: Privilege,
    val username: String
)