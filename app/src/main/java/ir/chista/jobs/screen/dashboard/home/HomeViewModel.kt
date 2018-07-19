package ir.chista.jobs.screen.dashboard.home

import android.arch.lifecycle.LiveData
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.data.model.LocalLogin
import ir.chista.jobs.data.model.UserRole
import ir.chista.jobs.data.model.isNotEmpty
import ir.chista.jobs.dialog.ChangeUserTitleDialog
import ir.chista.jobs.dialog.LogoutDialog
import ir.chista.jobs.screen.loading.LoadingActivity
import ir.chista.jobs.screen.login.LoginActivity
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.launchActivity
import ir.chista.util.LiveDatas.map
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ir.chista.util.Observables.toLiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import java.lang.ref.WeakReference

class HomeViewModel : BaseViewModel() {
  val user: LiveData<LocalLogin> = AccountManager.observable.toLiveData()

  fun init() {
  }

}
