package com.locked.shingranicommunity.session

import androidx.lifecycle.LiveData
import com.locked.shingranicommunity.models.User

interface Session {

    val loginState: LiveData<Boolean>

    fun getToken(): String

    fun getUser(): User?

    fun getUserId(): String?

    fun getFullName(): String?

    fun getUsername(): String?

    fun getAppId(): String

    fun getEventTemplateId(): String

    fun getAnnouncementTemplateId(): String

    fun isUserAdmin(): Boolean

    fun isLoggedIn(): Boolean
}