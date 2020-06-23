package com.locked.shingranicommunity.locked.models.request

/*
    {
        "state" : "Blocked"
    }
 */
class BlockRequestBody() {
    val state: String = com.locked.shingranicommunity.locked.models.MemberState.BLOCKED.state
}