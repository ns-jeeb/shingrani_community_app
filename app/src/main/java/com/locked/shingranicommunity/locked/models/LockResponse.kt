package com.locked.shingranicommunity.locked.models

open class LockResponse {
    var message: String? = null
    var errorType: String? = null
    var errors: List<Error> = mutableListOf()
}