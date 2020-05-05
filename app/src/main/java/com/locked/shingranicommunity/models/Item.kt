package com.locked.shingranicommunity.models

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

open class Item {
    private var onStatusChanged: MutableSet<((String?, String?) -> Unit)> = mutableSetOf()
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
}

open class EventItem() : Item() {
    val name: String? by Delegate(fields, "Name")
    val type: String? by Delegate(fields, "Type")
    val address: String? by Delegate(fields, "Address")
    val time: String? by Delegate(fields, "Time")
    val detail: String? by Delegate(fields, "Detail")
    val accepted: String? by Delegate(fields, "Accepted")
    val rejected: String? by Delegate(fields, "Rejected")
}

open class AnnouncementItem : Item() {
    val title: String? by Delegate(fields, "Title")
    val text: String? by Delegate(fields, "Text")
    val timestamp: String? by Delegate(fields, "Timestamp")
}

class Delegate(val fields: MutableList<Field>, val propName: String) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String? {
        for (field in fields) {
            if (field.name == propName) {
                return field.value
            }
        }
        return null
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
        var foundField: Field? = null
        for (field in fields) {
            if (field.name == propName) {
                foundField = field
                break
            }
        }
        if (foundField == null) {
            foundField = Field(propName)
            fields.add(foundField)
        }
        foundField.value = value
    }


}

class Status {
    companion object {
        val CREATED = "CREATED"
        val CREATE_FAILED = "CREATE_FAILED"
        val DELETED = "DELETED"
        val DELETE_FAILED = "DELETE_FAILED"
    }
}