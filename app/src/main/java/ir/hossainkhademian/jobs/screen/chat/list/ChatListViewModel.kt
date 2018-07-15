package ir.hossainkhademian.jobs.screen.chat.list

import android.arch.lifecycle.ViewModel
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.util.Observables.toLiveData

internal class ChatListViewModel : ViewModel() {
  val userChats = Repository.getUserChats().toLiveData()
}
