package com.locked.shingranicommunity.locked.models

/*
    {
        "_id": "5e860cc8ff354415000ec5e5",
        "app": "5d4a348f88fb44130084f903",
        "email": "n@g.com",
        "state": "Joined", // Pending/Invited/Joined/Blocked
        "user": null,
        "__v": 0
    }
*/

class Member(val _id: String,
             val app: String,
             val email: String,
             val state: String,
             val user: User?) {

    @Transient var isMe: Boolean = false
    @Transient var isAdmin: Boolean = false
}

enum class MemberState(val state: String) {
    PENDING("Pending"),
    INVITED("Invited"),
    JOINED("Joined"),
    BLOCKED("Blocked")
}