package com.locked.shingranicommunity.members

data class ShingraniMember(
    val __v: Int,
    val _id: String,
    val app: String,
    val email: String,
    val state: String,
    val user: User
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