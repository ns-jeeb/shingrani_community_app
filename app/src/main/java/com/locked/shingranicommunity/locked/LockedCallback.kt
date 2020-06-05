package com.locked.shingranicommunity.locked

import com.google.gson.Gson
import com.locked.shingranicommunity.locked.models.Error
import com.locked.shingranicommunity.locked.models.LockResponse
import com.locked.shingranicommunity.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

abstract class LockedCallback<T>(val formatError: Boolean = true) : Callback<T> {

    override fun onFailure(call: Call<T>, t: Throwable) {
        fail(formatFailMessage("System Error occurred"))
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful && response.body() != null) {
            success(response.body()!!)
        } else if (response.errorBody() != null) {
            val error: LockResponse = Gson().fromJson(response.errorBody()!!.charStream(), LockResponse::class.java)
            fail(formatFailMessage(error.message, error.errors), error.errors)
        } else {
            fail(formatFailMessage("System Error occurred"))
        }
    }

    @Suppress("UselessCallOnNotNull")
    open fun formatFailMessage(message: String?, details: List<Error> = emptyList()) : String {
        var _message: String = message?.trim() ?: ""
        if (formatError && !details.isNullOrEmpty()) {
            for (detail in details) {
                if (!detail.message.trim().isNullOrEmpty()) {
                    var _detailMessage = detail.message.trim()
                    if (!_message.endsWith(".")) {
                        _message = _message.plus(". ")
                    }
                    _message = _message.plus(_detailMessage)
                }
            }
        }
        return _message.trim()
    }

    abstract fun success(response: T)
    abstract fun fail(message: String, details: List<Error> = emptyList())
}