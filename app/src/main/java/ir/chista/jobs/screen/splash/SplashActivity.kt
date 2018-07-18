package ir.chista.jobs.screen.splash

import android.os.Bundle
import android.os.Handler
import ir.chista.jobs.R
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.data.model.isLoggedIn
import ir.chista.jobs.util.BaseActivity
import ir.chista.jobs.screen.loading.LoadingActivity
import ir.chista.jobs.screen.register.RegisterActivity
import ir.chista.util.launchActivity


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
