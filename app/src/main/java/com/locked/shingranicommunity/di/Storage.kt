
package com.locked.shingranicommunity.di

import androidx.lifecycle.MutableLiveData
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.models.TemplateModel

interface Storage {
    fun setToken(key: String, value: String)
    fun getToken(key: String): String

    fun setCurrentUser(user: User)
    fun getCurrentUser(): User?

//    fun getUser(): MutableLiveData<ArrayList<ShingraniMember>>
//    fun setUser(users: ArrayList<ShingraniMember>)

    fun getTemplateModel(): TemplateModel?
    fun setTemplateModel(templateModel: TemplateModel)
}
