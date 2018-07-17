package ir.hossainkhademian.jobs.screen.chat.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.jobs.util.BaseViewModel
import ir.hossainkhademian.util.Observables.toLiveData

internal class ChatListViewModel : BaseViewModel() {
  val contact = Repository.Chats.listUserChats().toLiveData()
  val userChats = Repository.Chats.listUserChats().toLiveData()
  val selectedId: LiveData<ID> = MutableLiveData()

  fun init() {
    this.selectedId as MutableLiveData

    this.selectedId.postValue(emptyID)
  }

  fun postSelectedId(id: ID) {
    (selectedId as MutableLiveData).postValue(id)
  }
}
