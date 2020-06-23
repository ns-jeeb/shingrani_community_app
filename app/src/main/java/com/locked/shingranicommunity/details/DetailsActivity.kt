package com.locked.shingranicommunity.details

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.UrCommunityApplication
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.di2.item_details.ItemDetailsComponent
import com.locked.shingranicommunity.search_map.ItemDetailsComponentProvider

class DetailsActivity : FragmentActivity(), ItemDetailsComponentProvider {

    override lateinit var itemDetailsComponent: ItemDetailsComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        itemDetailsComponent = UrCommunityApplication.instance.appComponent2.itemDetailsComponent.create(this)
        itemDetailsComponent.inject(this)
        super.onCreate(savedInstanceState)
    }
}
