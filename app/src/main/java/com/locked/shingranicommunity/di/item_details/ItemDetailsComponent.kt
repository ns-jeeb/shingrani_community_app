package com.locked.shingranicommunity.di2.item_details

import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.details.DetailsActivity
import com.locked.shingranicommunity.details.DetailsFragment
import com.locked.shingranicommunity.di.item_details.ItemDetailsModule
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