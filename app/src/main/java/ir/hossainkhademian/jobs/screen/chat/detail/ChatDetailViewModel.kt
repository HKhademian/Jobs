package ir.hossainkhademian.jobs.screen.chat.detail

import android.arch.lifecycle.*
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.Chat
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.isEmpty
import ir.hossainkhademian.jobs.data.model.isReceived
import ir.hossainkhademian.util.LiveDatas
import ir.hossainkhademian.util.LiveDatas.map
import ir.hossainkhademian.util.Observables.toLiveData
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

internal class ChatDetailViewModel(val app: App, val userId: ID) : AndroidViewModel(app) {
  val chats = Repository.getChatsByContact(userId).toLiveData()

  val messageField: LiveData<String> = MutableLiveData<String>().apply { postValue("") }

  val isSending: LiveData<Boolean> = MutableLiveData<Boolean>().apply { postValue(false) }

  val sendEnabled = LiveDatas.zip(messageField, isSending)
    .map { (message, isSending) -> message.isNotBlank() && !isSending }


  fun send(error: (e: Exception) -> Unit = {}): Job? {
    val message = messageField.value ?: ""
    if (message.isBlank())
      return null

    (isSending as MutableLiveData)
    (messageField as MutableLiveData)


    isSending.postValue(true)

    return launch {
      try {
        Repository.chatSend(userId, message)

        launch(UI) {
          messageField.postValue("")
        }
      } catch (e: Exception) {
        launch(UI) { error(e) }
      }
      launch(UI) {
        isSending.postValue(false)
      }
    }
  }

  fun markAsSeen(item: Chat, error: (e: Exception) -> Unit = {}): Job? {
    if (item.isEmpty || !item.isReceived || !item.unseen)
      return null

    return launch {
      try {
        Repository.chatSeen(item.id)
      } catch (e: Exception) {
        launch(UI) { error(e) }
      }
    }
  }

  class Factory(private val app: App, private val userId: ID) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
      ChatDetailViewModel(app, userId) as T
  }
}
