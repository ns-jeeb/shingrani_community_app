package com.locked.shingranicommunity.locked.models

open class AnnouncementItem : Item() {
    @delegate:Transient var title: String? by ItemDelegate(
        this::fields,
        "Title"
    )
    @delegate:Transient var text: String? by ItemDelegate(
        this::fields,
        "Text"
    )
    @delegate:Transient var timestamp: String? by ItemDelegate(
        this::fields,
        "Timestamp"
    )
}

enum class AnnouncementStatus() {
    CREATED,
    CREATE_FAILED,
    DELETED,
    DELETE_FAILED
}