package com.locked.shingranicommunity.locked.models

open class EventItem() : Item() {
    @delegate:Transient var name: String? by ItemDelegate(
        this::fields,
        "Name"
    )
    @delegate:Transient var type: String? by ItemDelegate(
        this::fields,
        "Type"
    )
    @delegate:Transient var address: String? by ItemDelegate(
        this::fields,
        "Address"
    )
    @delegate:Transient var time: String? by ItemDelegate(
        this::fields,
        "Time"
    )
    @delegate:Transient var detail: String? by ItemDelegate(
        this::fields,
        "Detail"
    )
    @delegate:Transient var accepted: String? by ItemDelegate(
        this::fields,
        "Accepted"
    )
    @delegate:Transient var rejected: String? by ItemDelegate(
        this::fields,
        "Rejected"
    )
}

enum class EventStatus() {
    CREATED,
    CREATE_FAILED,
    DELETED,
    DELETE_FAILED,
    ACCEPTED,
    ACCEPT_FAILED,
    REJECTED,
    REJECT_FAILED
}

enum class EventType(val type: String) {
    EVENT("Event"),
    WEDDING("Wedding"),
    ENGAGEMENT("Engagement"),
    PARTY("Party"),
    BIRTHDAY("Birthday"),
    FUNDRAISING("Fundraising"),
    FUNERAL("Funeral"),
    GATHERING("Gathering"),
    MEETING("Meeting"),
    OUTING("Outing")
}