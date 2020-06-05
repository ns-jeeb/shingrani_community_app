package com.locked.shingranicommunity.di2.item_details

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.dashboard.event.details.DetailsActivity
import com.locked.shingranicommunity.dashboard.event.details.DetailsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@ItemDetailsScope
@Subcomponent(modules = [ItemDetailsModule::class])
interface ItemDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : ItemDetailsComponent
    }
    fun inject(activity: DetailsActivity)
    fun inject(fragment: DetailsFragment)
}