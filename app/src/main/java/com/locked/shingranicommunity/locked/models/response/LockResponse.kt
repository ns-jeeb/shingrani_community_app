package com.locked.shingranicommunity.locked.models.response

import com.locked.shingranicommunity.locked.models.Error

open class LockResponse {
    var message: String? = null
    var errorType: String? = null
    var errors: List<Error> = mutableListOf()
}