package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import com.locked.shingranicommunity.registration_login.registration.enterdetails.EnterDetailsFragment
import dagger.Subcomponent
import javax.inject.Singleton

@ActivityScope
@Subcomponent
interface RegisterComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): RegisterComponent
    }

    fun inject(activity: RegistrationActivity)
    fun inject(fragment: EnterDetailsFragment)

}