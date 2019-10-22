package com.locked.shingranicommunity.dashboard.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.dashboard.announncement.Announcement
import java.util.ArrayList

class EventViewModel : ViewModel() {
    private var _event = MutableLiveData<List<Announcement>>()
    val event: MutableLiveData<List<Announcement>> get() = _event


    init {
        _event = getAnnounceList()
    }

    fun getAnnounceList(): MutableLiveData<List<Announcement>> {
        val announce: ArrayList<Announcement> = ArrayList()

        val announcement = Announcement()
        val announcement1 = Announcement()
        val announcement2 = Announcement()
        val announcement3 = Announcement()
        val announcement4 = Announcement()
        val announcement5 = Announcement()

        announcement.name = "Wedding of Emily and James"
        announcement.type = "you are invited to this new couple for the fabulous party"

        announcement2.name = "1 Wedding of Emily and James"
        announcement2.type = "1 you are invited to this new couple for the fabulous party"

        announcement1.name = "2 Wedding of Emily and James"
        announcement1.type = "2 you are invited to this new couple for the fabulous party"
        announcement3.name = "2 Wedding of Emily and James"
        announcement3.type = "2 you are invited to this new couple for the fabulous party"
        announcement4.name = "2 Wedding of Emily and James"
        announcement4.type = "2 you are invited to this new couple for the fabulous party"
        announcement5.name = "2 Wedding of Emily and James"
        announcement5.type = "2 you are invited to this new couple for the fabulous party"

        announce.add(announcement)
        announce.add(announcement1)
        announce.add(announcement2)
        announce.add(announcement3)
        announce.add(announcement4)
        announce.add(announcement5)
        _event.value = announce
        return _event
    }
}
