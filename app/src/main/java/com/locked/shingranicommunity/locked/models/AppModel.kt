package com.locked.shingranicommunity.locked.models

data class AppModel(
    val _id: String,
    val admins: List<User>,
    val description: String,
    val name: String,
    val registrationConfig: RegistrationConfig,
    val templates: List<Template>
)