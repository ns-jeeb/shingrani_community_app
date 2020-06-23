package com.locked.shingranicommunity.locked.models.response

class CreateResponse<T>: LockResponse() {
    var item: T? = null
}