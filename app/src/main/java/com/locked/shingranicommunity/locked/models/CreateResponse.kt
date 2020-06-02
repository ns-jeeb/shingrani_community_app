package com.locked.shingranicommunity.locked.models

class CreateResponse<T>: LockResponse() {
    var item: T? = null
}