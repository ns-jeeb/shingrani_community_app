package com.locked.shingranicommunity.member

import androidx.lifecycle.*
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.locked.models.Member
import com.locked.shingranicommunity.locked.models.MemberState
import com.locked.shingranicommunity.repositories.MemberRepository
import com.locked.shingranicommunity.session.Session
import javax.inject.Inject

class MemberListViewModel @Inject constructor(
    private val repository: MemberRepository,
    private val session: Session,
    private val navigation: Navigation,
    private val res: ResourceProvider) : ViewModel() {

    val pageTitle: String = res.getString(R.string.action_member)
    private val data: Data = Data()
    private val itemViewModelList: MutableMap<Member, ItemViewModel> = mutableMapOf()
    val message: LiveData<String> = data.message
    val list: LiveData<List<Member>> = data.list
    val showInvite: LiveData<Boolean> = data.showInvite

    val observeAuthError: Observer<Boolean> = Observer {
        it?.let { authErrorOccurred(it) }
    }
    val observeAppState: Observer<Boolean> = Observer {
        it?.let { data.showInvite.value = session.isUserAdmin() }
    }

    init {
        data.list.addSource(repository.members) { result ->
            data.list.value = result
                .filterNot { it.state == MemberState.BLOCKED.state && !session.isUserAdmin() }
                .sortedWith(compareBy( {!it.isAdmin}, {!it.isMe}, {it.email}))
        }
        repository.authError.observeForever(observeAuthError)
        session.appState.observeForever(observeAppState)
    }

    private fun authErrorOccurred(it: Boolean?) {
        it?.let {
            if (it) {
                navigation.navigateToLogin(true)
            }
        }
    }

    fun load() {
        repository.fetchMembers()
    }

    fun messageHandled() {
        data.message.value = null
    }

    override fun onCleared() {
        repository.authError.removeObserver(observeAuthError)
        session.appState.removeObserver(observeAppState)
        clearItemViewModels()
        super.onCleared()
    }

    private fun clearItemViewModels() {
        for (entry in itemViewModelList) {
            entry.value.onCleared()
        }
    }

    // use this onBind() of List Adapter
    fun getItemViewModel(itemIndex: Int): ItemViewModel? {
        val memberItem: Member? = list.value!!.getOrNull(itemIndex)
        memberItem?.let {
            var itemViewModel: ItemViewModel? = itemViewModelList.get(memberItem)
            if (itemViewModel == null) {
                itemViewModel = ItemViewModel(memberItem, session, navigation, repository)
                itemViewModelList.put(memberItem, itemViewModel)
            }
            return itemViewModel
        }
        return null
    }

    fun invitePressed() {
        navigation.navigateToInvite(true)
    }

    private data class Data(
        val message: MutableLiveData<String> = MutableLiveData<String>()) {
        val list: MediatorLiveData<List<Member>> = MediatorLiveData()
        val showInvite: MutableLiveData<Boolean> = MutableLiveData(false)
    }

    inner class ItemViewModel(val member: Member,
                              private val session: Session,
                              private val navigation: Navigation,
                              private val repository: MemberRepository) {

        val blockConfirmationTitle: String = res.getString(R.string.member_block_confirmation_title)
        val blockConfirmationDesc: String = res.getString(R.string.member_block_confirmation_desc).format(member.email)

        val data: ItemData = ItemData()
        val title: LiveData<String> = data.title
        val phoneNumber: LiveData<String> = data.phoneNumber
        val showAdmin: LiveData<Boolean> = data.showAdmin
        val showBlocked: LiveData<Boolean> = data.showBlocked
        val showInvited: LiveData<Boolean> = data.showInvited
        val showPhoneAction: LiveData<Boolean> = data.showPhoneAction
        val showTextAction: LiveData<Boolean> = data.showTextAction
        val showEmailAction: LiveData<Boolean> = data.showEmailAction
        val showSettingsAction: LiveData<Boolean> = data.showSettingsAction
        val showBlockAction: LiveData<Boolean> = data.showBlockAction
        val showUnblockAction: LiveData<Boolean> = data.showUnblockAction
        val showBlockConfirmation: LiveData<Boolean> = data.showBlockConfirmation

        init {
            // TITLE
            var title = member.user?.name?.capitalize() ?: member.email
            if (member.isMe) {
                title += " " + res.getString(R.string.is_me)
            }
            data.title.value = title
            // ADMIN
            data.showAdmin.value = member.isAdmin
            // PHONE
            member.user?.hideNumber?.let { data.showPhoneAction.value = !it}
            // TEXT
            data.showTextAction.value = true
            //phone number
            data.phoneNumber.value = member.phoneNumber
            // EMAIL
            data.showEmailAction.value = member.email.isNotBlank() && !member.isMe
            // SETTINGS
            data.showSettingsAction.value = member.isMe
            // BLOCK
            data.showBlockAction.value = session.isUserAdmin() && !member.isAdmin && member.state == MemberState.JOINED.state
            data.showUnblockAction.value = session.isUserAdmin() && !member.isAdmin && member.state == MemberState.BLOCKED.state
            data.showBlocked.value = session.isUserAdmin() && !member.isAdmin && member.state == MemberState.BLOCKED.state
            // INVITED
            data.showInvited.value = !member.isAdmin && member.state == MemberState.INVITED.state
        }

        fun block(confirmed: Boolean = false) {
            if (confirmed) {
                data.showBlockConfirmation.value = false
                repository.blockMember(member) {
                    if (it.success) {
                        this@MemberListViewModel.data.message.postValue(res.getString(R.string.blocked_success).format(member.email))
                    } else {
                        this@MemberListViewModel.data.message.postValue(res.getString(R.string.blocked_failed).format(member.email))
                    }
                }
            } else {
                data.showBlockConfirmation.value = true
            }
        }

        fun unblock() {
            repository.unblockMember(member) {
                if (it.success) {
                    this@MemberListViewModel.data.message.postValue(res.getString(R.string.unblock_success).format(member.email))
                } else {
                    this@MemberListViewModel.data.message.postValue(res.getString(R.string.unblock_failed).format(member.email))
                }
            }
        }

        fun cancelBlock() {
            data.showBlockConfirmation.value = false
        }

        fun sendEmail() {
            navigation.sendEmail(member.email)
        }

        fun makePhoneCall() {
            if (!member.isMe && !member.phoneNumber.isNullOrEmpty()){
                navigation.makePhoneCall(member.phoneNumber)
            }
        }

        fun settings() {
            navigation.navigateToSettings(true)
        }

        fun onCleared() {}
    }

    data class ItemData(
        val title: MutableLiveData<String> = MutableLiveData(""),
        val phoneNumber: MutableLiveData<String> = MutableLiveData(""),
        val showAdmin: MutableLiveData<Boolean> = MutableLiveData(false),
        val showBlocked: MutableLiveData<Boolean> = MutableLiveData(false),
        val showInvited: MutableLiveData<Boolean> = MutableLiveData(false),
        val showPhoneAction: MutableLiveData<Boolean> = MutableLiveData(false),
        val showTextAction: MutableLiveData<Boolean> = MutableLiveData(false),
        val showEmailAction: MutableLiveData<Boolean> = MutableLiveData(false),
        val showSettingsAction: MutableLiveData<Boolean> = MutableLiveData(false),
        val showBlockAction: MutableLiveData<Boolean> = MutableLiveData(false),
        val showUnblockAction: MutableLiveData<Boolean> = MutableLiveData(false),
        val showBlockConfirmation: MutableLiveData<Boolean> = MutableLiveData(false)
    )
}
