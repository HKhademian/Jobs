package ir.chista.jobs.screen.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ir.chista.jobs.R
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.screen.loading.LoadingActivity
import ir.chista.jobs.screen.login.LoginActivity
import ir.chista.jobs.screen.register.RegisterActivity
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.launchActivity
import kotlinx.coroutines.experimental.*

class LoginViewModel : BaseViewModel() {
  val phoneError: LiveData<Int> = MutableLiveData()
  val passwordError: LiveData<Int> = MutableLiveData()
  val isSending: LiveData<Boolean> = MutableLiveData()
  private var loginJob: Job? = null

  fun init() {
    phoneError as MutableLiveData
    passwordError as MutableLiveData
    isSending as MutableLiveData

    phoneError.value = 0
    passwordError.value = 0
    isSending.value = false
  }

  fun register(phone: String) {
    postActivityTask { activity ->
      activity.launchActivity<RegisterActivity>(extras = *arrayOf(
        LoginActivity.EXTRA_PHONE to phone
      ))
    }
  }

  fun login(phone: String, password: String) {
    phoneError as MutableLiveData
    passwordError as MutableLiveData
    isSending as MutableLiveData

    var error = false
    if (phone.length <= 3) {
      phoneError.value = R.string.error_field_required
      error = true
    } else if (!AccountManager.isPhoneValid(phone)) {
      phoneError.value = R.string.error_invalid_phone
      error = true
    }
    if (password.isEmpty()) {
      passwordError.value = R.string.error_invalid_password
      error = true
    }
    if (error) return

    phoneError.postValue(0)
    passwordError.postValue(0)

    loginJob?.cancel()
    isSending.postValue(true)
    loginJob = launch(CommonPool + CoroutineExceptionHandler { _, ex -> postError(ex); isSending.postValue(false) }) {
      AccountManager.login(phone, password)
      isSending.postValue(false)

      postActivityTask { activity ->
        activity.launchActivity<LoadingActivity>()
      }

      loginJob = null
    }
  }


}
