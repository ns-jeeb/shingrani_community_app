package com.locked.shingranicommunity.session

import androidx.lifecycle.LiveData
import com.locked.shingranicommunity.locked.models.User

interface Session {

    val loginState: LiveData<Boolean>
    val appState: LiveData<Boolean>

    fun getToken(): String

    fun getUser(): User?

    fun getUserId(): String?

    fun getFullName(): String?

    fun getUsername(): String?

    fun getAppId(): String

    fun getEventTemplateId(): String

    fun getAnnouncementTemplateId(): String

    fun getAdminList(): List<User>

    fun isUserAdmin(): Boolean

    fun isLoggedIn(): Boolean

    fun isMe(user: User?): Boolean

    fun isAdmin(user: User?): Boolean
}