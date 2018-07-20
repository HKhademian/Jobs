package ir.chista.jobs.screen.register

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ir.chista.jobs.R
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.screen.loading.LoadingActivity
import ir.chista.jobs.screen.login.LoginActivity
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.launchActivity
import kotlinx.coroutines.experimental.*

class RegisterViewModel : BaseViewModel() {
  val phoneError: LiveData<Int> = MutableLiveData()
  val titleError: LiveData<Int> = MutableLiveData()
  val isSending: LiveData<Boolean> = MutableLiveData()
  private var registerJob: Job? = null

  fun init() {
    phoneError as MutableLiveData
    titleError as MutableLiveData
    isSending as MutableLiveData

    phoneError.value = 0
    titleError.value = 0
    isSending.value = false
  }

  fun login(phone: String) {
    postActivityTask { activity ->
      activity.launchActivity<LoginActivity>(extras = *arrayOf(
        LoginActivity.EXTRA_PHONE to phone
      ))
    }
  }

  fun register(phone: String, title: String) {
    phoneError as MutableLiveData
    titleError as MutableLiveData
    isSending as MutableLiveData

    var error = false
    if (phone.length <= 3) {
      phoneError.value = R.string.error_field_required
      error = true
    } else if (!AccountManager.isPhoneValid(phone)) {
      phoneError.value = R.string.error_invalid_phone
      error = true
    }
    if (title.isEmpty()) {
      titleError.value = R.string.error_invalid_title
      error = true
    }
    if (error) return

    phoneError.postValue(0)
    titleError.postValue(0)

    registerJob?.cancel()
    isSending.postValue(true)
    registerJob = launch(CommonPool + CoroutineExceptionHandler { _, ex -> postError(ex); isSending.postValue(false) }) {
      AccountManager.register(phone, title)
      isSending.postValue(false)

      postActivityTask { activity ->
        //activity.launchActivity<LoadingActivity>()
        activity.launchActivity<LoginActivity>(extras = *arrayOf(
          LoginActivity.EXTRA_PHONE to phone
        ))
        activity.finish()
      }

      registerJob = null
    }
  }


}
