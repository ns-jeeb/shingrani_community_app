package com.locked.shingranicommunity.models

import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

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
}

open class EventItem() : Item() {
    @delegate:Transient val name: String? by Delegate(this::fields, "Name")
    @delegate:Transient val type: String? by Delegate(this::fields, "Type")
    @delegate:Transient val address: String? by Delegate(this::fields, "Address")
    @delegate:Transient val time: String? by Delegate(this::fields, "Time")
    @delegate:Transient val detail: String? by Delegate(this::fields, "Detail")
    @delegate:Transient val accepted: String? by Delegate(this::fields, "Accepted")
    @delegate:Transient val rejected: String? by Delegate(this::fields, "Rejected")
}

open class AnnouncementItem : Item() {
    @delegate:Transient val title: String? by Delegate(this::fields, "Title")
    @delegate:Transient val text: String? by Delegate(this::fields, "Text")
    @delegate:Transient val timestamp: String? by Delegate(this::fields, "Timestamp")
}

class Delegate(val fields: KMutableProperty0<MutableList<Field>>, val propName: String) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String? {
        for (field in fields.get()) {
            if (field.name != null && field.name!!.toLowerCase() == propName.toLowerCase()) {
                return field.value
            }
        }
        return null
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
        var foundField: Field? = null
        for (field in fields.get()) {
            if (field.name != null && field.name!!.toLowerCase() == propName.toLowerCase()) {
                foundField = field
                break
            }
        }
        if (foundField == null) {
            foundField = Field(propName)
            fields.get().add(foundField)
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