package com.locked.shingranicommunity.locked.models

class LockResponse {
    var message: String? = null
    var errorType: String? = null
    var errors: List<Error> = mutableListOf()
}