package ir.hossainkhademian.jobs.screen.splash

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.View
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.data.DataManager
import ir.hossainkhademian.jobs.data.model.isLoggedIn
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.dashboard.DashboardActivity
import ir.hossainkhademian.jobs.screen.loading.LoadingActivity
import ir.hossainkhademian.jobs.screen.register.RegisterActivity
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class SplashActivity : BaseActivity() {
  private val SPLASH_DELAY = 1000L // 15000L //
  private val handler = Handler()
  private val launcher = Runnable(::launch)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
  }

  override fun onStart() {
    super.onStart()
    handler.postDelayed(launcher, SPLASH_DELAY)
  }

  override fun onStop() {
    handler.removeCallbacks(launcher)
    super.onStop()
  }

  private fun launch() {
    handler.removeCallbacks(launcher)

    if (isFinishing)
      return

    launchActivity<LoadingActivity>()


    if (AccountManager.user.isLoggedIn)
      launchActivity<LoadingActivity>()
    else
      launchActivity<RegisterActivity>()

    finish()
  }
}
