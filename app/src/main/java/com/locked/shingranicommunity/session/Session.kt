package com.locked.shingranicommunity.session

import com.locked.shingranicommunity.models.User

interface Session {

    fun getToken(): String

    fun getUser(): User?

    fun getUserId(): String?

    fun getFullName(): String?

    fun getUsername(): String?

    fun isUserAdmin(): Boolean

    fun isLoggedIn(): Boolean
}