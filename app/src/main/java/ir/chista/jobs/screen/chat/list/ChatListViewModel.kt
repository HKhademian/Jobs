package ir.chista.jobs.screen.chat.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ir.chista.jobs.data.DataManager
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.emptyID
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.Observables.toLiveData

internal class ChatListViewModel : BaseViewModel() {
  val isOnline: LiveData<Boolean> = DataManager.isOnline.toLiveData()
  val contact = Repository.Chats.listUserChats().toLiveData()
  val userChats = Repository.Chats.listUserChats().toLiveData()
  val selectedId: LiveData<ID> = MutableLiveData()

  fun init(selectedId: ID = emptyID) {
    this.selectedId as MutableLiveData

    this.selectedId.postValue(selectedId)
  }

  fun postSelectedId(id: ID) {
    (selectedId as MutableLiveData).postValue(id)
  }
}
