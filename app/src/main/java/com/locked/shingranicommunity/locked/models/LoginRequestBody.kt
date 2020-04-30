package com.locked.shingranicommunity.locked.models

data class LoginRequestBody(val username: String, val password: String)
data class RegisterRequestBody(val name: String,val password: String,val username: String)
