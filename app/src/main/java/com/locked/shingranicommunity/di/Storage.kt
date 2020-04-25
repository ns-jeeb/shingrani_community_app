
package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.models.AppModel

interface Storage {
    fun setToken(key: String, value: String)
    fun getToken(key: String): String

    fun setCurrentUser(user: User)
    fun getCurrentUser(): User?

//    fun getUser(): MutableLiveData<ArrayList<ShingraniMember>>
//    fun setUser(users: ArrayList<ShingraniMember>)

    fun getAppModel(): AppModel?
    fun setAppModel(appModel: AppModel)
}
