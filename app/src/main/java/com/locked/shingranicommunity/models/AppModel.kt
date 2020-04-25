package com.locked.shingranicommunity.models

data class AppModel(
    val __v: Int,
    val _id: String,
    val admins: List<Admin>,
    val createdAt: String,
    val creator: Any,
    val dataAccessType: String,
    val description: String,
    val name: String,
    val owner: Any,
    val registrationConfig: RegistrationConfig,
    val templates: List<Template>,
    val updatedAt: String
)

data class Admin(
    val _id: String,
    val name: String,
    val username: String
)

data class RegistrationConfig(
    val approvalRequired: Boolean,
    val invitable: Boolean,
    val joinable: Boolean
)

data class Template(
    val __v: Int,
    val _id: String,
    val app: String,
    val colorDarker: String,
    val colorOriginal: String,
    val createdAt: String,
    val creator: String,
    val description: String,
    val fields: List<Field>,
    val icon: Icon,
    val owner: String,
    val rules: List<Any>,
    val tags: List<Any>,
    val title: String,
    val updatedAt: String
)

data class Field(
    val example: Any,
    val hidden: Boolean,
    val important: Boolean,
    val name: String,
    val required: Boolean,
    val sensitive: Boolean,
    val type: String,
    val value: String
)

data class Icon(
    val url: String
)