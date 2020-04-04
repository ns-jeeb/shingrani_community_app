package com.locked.shingranicommunity.members

sealed class MemberResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : MemberResult<T>()
    data class Error(val error: Int?) : MemberResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "error $error"
        }
    }
}