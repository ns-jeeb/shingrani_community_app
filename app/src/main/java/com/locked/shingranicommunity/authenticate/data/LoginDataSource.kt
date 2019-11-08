package com.locked.shingranicommunity.authenticate.data

import android.content.Context
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

 data class LoginDataSource(val loginEvent: LoginEvent?) {

    fun login(user: User, token: String): Result<LoggedInUser> {
        try {
            var fakeUser = LoggedInUser(user, token )
            if (!token.isEmpty() ){
                val db = UserDatabase(CommunityApp.instance)
                GlobalScope.launch {
                    db.loginDao().insertAll(User(user._id,user.username,user.name,user.administrator,user.publicCreation))
                    var data = db.loginDao().getAll()

                    data?.forEach {
                        println(it)
                        fakeUser = LoggedInUser(data.get(0),token)
                        loginEvent?.onLoginSuccess()
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
        var sheredPrfes = CommunityApp.instance.getSharedPreferences("token", Context.MODE_PRIVATE)
         sheredPrfes.edit().putString("","").apply()
    }
    fun logingWithToken(user: LoggedInUser?, token: String): Result<LoggedInUser> {

        try {
            if (token.isNotEmpty() && token !==""){
                loginEvent?.onLoginSuccess()
            }
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }


    }
}


