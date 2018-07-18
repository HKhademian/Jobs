package ir.hossainkhademian.jobs.screen.dashboard

import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.screen.login.LoginActivity
import ir.hossainkhademian.jobs.util.BaseViewModel
import ir.hossainkhademian.util.launchActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class DashboardViewModel : BaseViewModel() {
  fun init() {
  }

  fun logout() {
    val phone = AccountManager.phone
    launch {
      AccountManager.logout()
      launch(UI) {
        postActivityTask { activity ->
          activity.launchActivity<LoginActivity>(extras = *arrayOf(LoginActivity.EXTRA_PHONE to phone))
          activity.finish()
        }
      }
    }
  }
}
