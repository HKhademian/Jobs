package ir.chista.jobs.screen.chat.detail

import android.arch.lifecycle.*
import com.vdurmont.emoji.EmojiParser
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.plusAssign
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.LiveDatas
import ir.chista.util.LiveDatas.map
import kotlinx.coroutines.experimental.*

internal class ChatDetailViewModel : BaseViewModel() {
  val contact: LiveData<LocalUser> = MutableLiveData()
  val chats: LiveData<List<LocalChat>> = MutableLiveData()
  val messageField: LiveData<String> = MutableLiveData()
  val isSending: LiveData<Boolean> = MutableLiveData()
  val sendEnabled = LiveDatas.zip(messageField, isSending)
    .map { (message, isSending) -> message.isNotBlank() && !isSending }

  var listener: ChatDetailListener? = null

  private var disposable: Disposable? = null
  var contactId: ID = emptyID
    set(contactId) {
      field = contactId
      contact as MutableLiveData
      chats as MutableLiveData

      disposable?.dispose()

      disposable = CompositeDisposable(
        Repository.Chats.getContact(contactId).subscribe {
          contact.postValue(it ?: EmptyUser)
        },
        Repository.Chats.listsByContact(contactId).subscribe {
          chats.postValue(it ?: emptyList())
          markAsSeen()
        }
      )
    }

  fun init(contactId: ID = emptyID) {
    messageField as MutableLiveData
    isSending as MutableLiveData

    this.contactId = contactId
    messageField.postValue("")
    isSending.postValue(false)
  }

  fun send(): DisposableHandle {
    val message = EmojiParser.parseToUnicode(messageField.value ?: "")
    error as MutableLiveData
    isSending as MutableLiveData
    messageField as MutableLiveData

    isSending.postValue(true)
    return launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      isSending.postValue(false)
      postError(ex)
    }) {
      Repository.Chats.send(contactId, message)
    }.invokeOnCompletion { ex ->
      if (ex != null) {
        postError(ex)
      } else {
        messageField.postValue("")
      }
      isSending.postValue(false)
    }
  }

  fun markAsSeen(): DisposableHandle {
    error as MutableLiveData

    return launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      postError(ex)
    }) {
      Repository.Chats.seen(contactId)
    }.invokeOnCompletion {

    }
  }

  fun onMessageChanged(text: String) {
    messageField as MutableLiveData
    messageField.postValue(text)
  }
}
