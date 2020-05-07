package com.locked.shingranicommunity.event

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.repositories.EventRepository
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class EventCreateViewModel @Inject constructor(
    private val repository: EventRepository,
    private val session: Session)
    : ViewModel() {


}