package com.locked.shingranicommunity.locked.models

data class Template(
    val _id: String,
    val app: String,
    val description: String,
    val fields: List<Field>,
    val title: String
)