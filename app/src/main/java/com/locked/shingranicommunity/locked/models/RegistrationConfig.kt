package com.locked.shingranicommunity.locked.models

data class RegistrationConfig(
    val approvalRequired: Boolean,
    val invitable: Boolean,
    val joinable: Boolean
)