package com.locked.shingranicommunity.member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ResourceProvider
import com.locked.shingranicommunity.locked.models.Member
import com.locked.shingranicommunity.repositories.MemberRepository
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

class MemberListViewModel @Inject constructor(
    private val repository: MemberRepository,
    private val session: SessionManager,
    private val navigation: Navigation,
    private val res: ResourceProvider) : ViewModel() {

    val pageTitle: String = res.getString(R.string.action_member)
    private val data: Data = Data()
    private val itemViewModelList: MutableMap<Member, ItemViewModel> = mutableMapOf()
    val message: LiveData<String> = data.message
    val list: LiveData<MutableList<Member>> = data.list

    private val fetchMembersObserver: Observer<in MemberRepository.Data> = Observer {
        it?.let {
            if (it.success) {
                // data loaded
                data.list.postValue(repository.members)
            } else {
                data.message.postValue(it.message)
            }
        }
    }

    init {
        repository.fetchMembers.observeForever(fetchMembersObserver)
        repository.authError.observeForever(Observer { authErrorOccurred(it) })
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
        repository.fetchMembers.removeObserver(fetchMembersObserver)
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
        val list: MutableLiveData<MutableList<Member>> = MutableLiveData(mutableListOf())
    }

    inner class ItemViewModel(private val member: Member,
                              private val session: SessionManager,
                              private val navigation: Navigation,
                              private val repository: MemberRepository) {

        val blockConfirmationTitle: String = res.getString(R.string.member_block_confirmation_title)
        val blockConfirmationDesc: String = res.getString(R.string.member_block_confirmation_desc).format(member.email)

        val data: ItemData = ItemData()
        val title: LiveData<String> = data.title
        val showAdmin: LiveData<Boolean> = data.showAdmin
        val showPhone: LiveData<Boolean> = data.showPhone
        val showText: LiveData<Boolean> = data.showText
        val showEmail: LiveData<Boolean> = data.showEmail
        val showBlock: LiveData<Boolean> = data.showBlock
        val showBlockConfirmation: LiveData<Boolean> = data.showBlockConfirmation

        init {
            // TITLE
            val title = member.user?.name?.capitalize() ?: member.email
            data.title.value = title
            // ADMIN
            data.showAdmin.value = member.isAdmin
            // PHONE
            data.showPhone.value = false
            // TEXT
            data.showText.value = false
            // EMAIL
            data.showEmail.value = member.email.isNotBlank()
            // BLOCK
            data.showBlock.value = session.isUserAdmin() && !member.isAdmin
        }

        fun block(confirmed: Boolean = false) {
            if (confirmed) {
                data.showBlockConfirmation.value = false
                repository.blockMember(member)
            } else {
                data.showBlockConfirmation.postValue(true)
            }
        }

        fun sendEmail() {
            navigation.sendEmail(member.email)
        }

        fun onCleared() {}
    }

    data class ItemData(
        val title: MutableLiveData<String> = MutableLiveData(""),
        val showAdmin: MutableLiveData<Boolean> = MutableLiveData(false),
        val showPhone: MutableLiveData<Boolean> = MutableLiveData(false),
        val showText: MutableLiveData<Boolean> = MutableLiveData(false),
        val showEmail: MutableLiveData<Boolean> = MutableLiveData(false),
        val showBlock: MutableLiveData<Boolean> = MutableLiveData(true),
        val showBlockConfirmation: MutableLiveData<Boolean> = MutableLiveData(false)
    )
}
