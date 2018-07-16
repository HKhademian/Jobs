package ir.hossainkhademian.jobs.screen.chat.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.util.Observables.toLiveData

internal class ChatListViewModel(selectedUserId: ID = emptyID) : ViewModel() {
  val userChats = Repository.Chats.listUserChats().toLiveData()
  val selectedUserId: LiveData<ID> = MutableLiveData()

  init {
    this.selectedUserId as MutableLiveData

    this.selectedUserId.postValue(selectedUserId)
  }
}
