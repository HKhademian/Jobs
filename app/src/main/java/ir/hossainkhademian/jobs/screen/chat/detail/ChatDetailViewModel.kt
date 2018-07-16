package ir.hossainkhademian.jobs.screen.chat.detail

import android.arch.lifecycle.*
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.util.LiveDatas
import ir.hossainkhademian.util.LiveDatas.map
import ir.hossainkhademian.util.Observables.toLiveData
import kotlinx.coroutines.experimental.*

internal class ChatDetailViewModel(val app: App, val userId: ID) : AndroidViewModel(app) {
  private val chatsObservable = Repository.Chats.listsByContact(userId)
  val chats = chatsObservable.doOnEach { markAsSeen() }.toLiveData()

  val error: LiveData<Throwable> = MutableLiveData()
  val messageField: LiveData<String> = MutableLiveData()
  val isSending: LiveData<Boolean> = MutableLiveData()
  val sendEnabled = LiveDatas.zip(messageField, isSending)
    .map { (message, isSending) -> message.isNotBlank() && !isSending }


  fun send(): DisposableHandle {
    val message = messageField.value ?: ""
    error as MutableLiveData
    isSending as MutableLiveData
    messageField as MutableLiveData

    isSending.postValue(true)
    return launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      isSending.postValue(false)
      error.postValue(ex)
    }) {
      Repository.Chats.send(userId, message)
    }.invokeOnCompletion {
      messageField.postValue("")
      isSending.postValue(false)
    }
  }

  fun markAsSeen(): DisposableHandle {
    error as MutableLiveData

    return launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      error.postValue(ex)
    }) {
      Repository.Chats.seen(userId)
    }.invokeOnCompletion {

    }
  }
}
