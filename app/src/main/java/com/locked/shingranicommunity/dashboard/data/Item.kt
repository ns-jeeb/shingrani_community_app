package com.locked.shingranicommunity.dashboard.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    var _id: String? = null,
    var updatedAt: String? = null,
    var createdAt: String? = null,
    var owner: String? = null,
    var creator: String? = null,
    var app: String? = null,
    var template: String? = null,
    var v: String? = null,
    var fields: ArrayList<Field>? = null,
    var icon: Icon? = null,
    var colorDarker: String? = null,
    var colorOriginal: String? = null,
    var errorMessage: String? ="",
    var successMes: String ="",
    var isAttending: Boolean? = false

): Parcelable{
}
@Parcelize
data class Field(

    var name: String? = null,
    var value: String? = null
    ):Parcelable {
}
@Parcelize
data class Icon( var url: String? = null):Parcelable {
}
data class ErrorBodyResponse(
    val errors: ArrayList<Error>
){

}
data class Error(
    var errorMessage : String
){

}
data class RsvpObject(
    val rsvp: Rsvp?
)
data class Rsvp(
    var type: String,
    var user: String
)
