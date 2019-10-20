package com.locked.shingranicommunity.authenticate.data

import com.locked.shingranicommunity.CommunityApp
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.data.model.LoggedInUser
import com.locked.shingranicommunity.tutorials.UserDatabase
import com.locked.shingranicommunity.tutorials.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

 data class LoginDataSource(val loginEvent: LoginEvent) {

    fun login(user: User, token: String): Result<LoggedInUser> {
        try {
            var fakeUser = LoggedInUser(user, token )
            if (!token.isEmpty() ){
                val db = UserDatabase(CommunityApp.instance)
                GlobalScope.launch {
                    db.loginDao().insertAll(User(user._id,user.username,token,user.name))
                    var data = db.loginDao().getAll()

                    data?.forEach {
                        println(it)
                        fakeUser = LoggedInUser(data.get(0),it.token)
                        loginEvent.onLoginSuccess(data.get(0),true)

                    }
                }
            }else{
                fakeUser = LoggedInUser(user, token )
            }



            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {

    }
}


