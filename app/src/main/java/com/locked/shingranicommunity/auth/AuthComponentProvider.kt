package com.locked.shingranicommunity.auth

import com.locked.shingranicommunity.di2.auth.AuthComponent

interface AuthComponentProvider {
    var authComponent: AuthComponent
}