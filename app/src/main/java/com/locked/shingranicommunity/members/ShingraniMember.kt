package com.locked.shingranicommunity.members

data class ShingraniMember(
    val __v: Int,
    val _id: String,
    val app: String,
    val email: String,
    val state: String,
    val user: User,
    val message: String,
    val token: String
)

data class User(
    val __v: Int,
    val _id: String,
    val name: String,
    val privilege: Privilege,
    val username: String
)

data class Privilege(
    val administrator: Boolean,
    val publicCreation: Boolean
)