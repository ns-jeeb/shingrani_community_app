package com.locked.shingranicommunity.di

import com.locked.shingranicommunity.di.scops.ActivityScope
import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import com.locked.shingranicommunity.registration_login.registration.enterdetails.EnterDetailsFragment
import dagger.Subcomponent

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