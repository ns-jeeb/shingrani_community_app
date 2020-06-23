package com.locked.shingranicommunity.locked.models

import kotlin.properties.Delegates

open class Item {
    @Transient
    private var onStatusChanged: MutableSet<((String?, String?) -> Unit)> = mutableSetOf()
    @delegate:Transient
    var status by Delegates.observable<String?>(null) { _, old, new ->
        for (callback in onStatusChanged) {
            callback.invoke(old, new)
        }
    }
    var _id: String? = null
    var updatedAt: String? = null
    var createdAt: String? = null
    var owner: String? = null
    var creator: String? = null
    var app: String? = null
    var template: String? = null
    var fields: MutableList<Field> = mutableListOf()
    fun observeStatus(observer: (String?, String?) -> Unit) {
        onStatusChanged.add(observer)
    }
    fun deObserveStatus(observer: (String?, String?) -> Unit) {
        onStatusChanged.remove(observer)
    }
    open fun update(item: Item) {
        item.let {
            _id = item._id
            updatedAt = item.updatedAt
            createdAt = item.createdAt
            owner = item.owner
            creator = item.creator
            app = item.app
            template = item.template
            fields = item.fields
        }
    }
}