package com.locked.shingranicommunity.auth

import com.locked.shingranicommunity.di.auth.AuthComponent

interface AuthComponentProvider {
    var authComponent: AuthComponent
}