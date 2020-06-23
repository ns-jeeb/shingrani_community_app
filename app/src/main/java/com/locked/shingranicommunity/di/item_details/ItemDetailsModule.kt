package com.locked.shingranicommunity.di.item_details

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.di.viewmodel.ViewModelKey
import com.locked.shingranicommunity.di2.item_details.ItemDetailsScope
import com.locked.shingranicommunity.navigation.DetailsNavigation
import com.locked.shingranicommunity.search_map.Navigation
import com.locked.shingranicommunity.search_map.ItemDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ItemDetailsModule {

    @Binds
    @ItemDetailsScope
    @IntoMap
    @ViewModelKey(ItemDetailsViewModel::class)
    abstract fun bindItemDetailsViewModel(viewModel: ItemDetailsViewModel): ViewModel

    @Binds
    @ItemDetailsScope
    abstract fun bindDetailsNavigation(navigation: DetailsNavigation) : Navigation
}