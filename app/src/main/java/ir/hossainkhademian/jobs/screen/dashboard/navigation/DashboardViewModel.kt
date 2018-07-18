package ir.hossainkhademian.jobs.screen.dashboard.navigation

import android.arch.lifecycle.LiveData
import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.data.model.LocalLogin
import ir.hossainkhademian.jobs.data.model.UserRole
import ir.hossainkhademian.jobs.data.model.isNotEmpty
import ir.hossainkhademian.jobs.dialog.ChangeUserTitleDialog
import ir.hossainkhademian.jobs.dialog.LogoutDialog
import ir.hossainkhademian.jobs.screen.loading.LoadingActivity
import ir.hossainkhademian.jobs.screen.login.LoginActivity
import ir.hossainkhademian.jobs.util.BaseViewModel
import ir.hossainkhademian.util.launchActivity
import ir.hossainkhademian.util.LiveDatas.map
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ir.hossainkhademian.util.Observables.toLiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import java.lang.ref.WeakReference

class DashboardViewModel : BaseViewModel() {
  val user: LiveData<LocalLogin> = AccountManager.observable.toLiveData()
  val title = user.map { if (it.isNotEmpty) it.title else "Unknown User" }
  val phone = user.map { if (it.isNotEmpty) it.phone else "Unknown phone number" }
  val role = user.map { if (it.isNotEmpty) it.role else UserRole.User }

  var listener: WeakReference<DashboardNavigationListener>? = null

  fun init() {
  }

  fun refresh() {
    listener?.get()?.onNavigationClose()
    postActivityTask { activity ->
      activity.launchActivity<LoadingActivity>()
    }
  }

  fun logout() {
    listener?.get()?.onNavigationClose()
    postActivityTask { activity ->

      LogoutDialog.show(activity, {}, {
        val phone = phone.value ?: ""
        launch {
          AccountManager.logout()
          launch(UI) {
            postActivityTask { activity ->
              activity.launchActivity<LoginActivity>(extras = *arrayOf(LoginActivity.EXTRA_PHONE to phone))
              activity.finish()
            }
          }
        }
      })

    }
  }

  fun edit() {
    listener?.get()?.onNavigationClose()
    postActivityTask { activity ->
      ChangeUserTitleDialog.show(activity, {}, { title ->
        launch(CommonPool + CoroutineExceptionHandler { _, ex -> postError(ex) }) {
          AccountManager.changeTitle(title)
        }
      })
    }
  }

  fun exit() {
    listener?.get()?.onNavigationClose()
    listener?.get()?.onNavigationExit()
  }
}
