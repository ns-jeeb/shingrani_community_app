package com.locked.shingranicommunity.locked.models

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

class ItemDelegate(val fields: KMutableProperty0<MutableList<Field>>, val propName: String) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String? {
        for (field in fields.get()) {
            if (field.name != null && field.name!!.toLowerCase() == propName.toLowerCase()) {
                return field.value
            }
        }
        return null
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String?) {
        var foundField: Field? = null
        for (field in fields.get()) {
            if (field.name != null && field.name!!.toLowerCase() == propName.toLowerCase()) {
                foundField = field
                break
            }
        }
        if (foundField == null) {
            foundField =
                Field(propName)
            fields.get().add(foundField)
        }
        foundField.value = value
    }
}