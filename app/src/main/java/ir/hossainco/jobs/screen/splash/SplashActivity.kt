package ir.hossainco.jobs.screen.splash

import android.os.Bundle
import android.os.Handler
import ir.hossainco.jobs.R
import ir.hossainco.jobs.data.AccountManager
import ir.hossainco.jobs.util.launchActivity
import ir.hossainco.jobs.screen.BaseActivity
import ir.hossainco.jobs.screen.dashboard.DashboardActivity
import ir.hossainco.jobs.screen.register.RegisterActivity


class SplashActivity : BaseActivity() {
	private val SPLASH_DELAY = 2500L
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

		if (!isFinishing) {
			if (AccountManager.isLoggedIn)
				launchActivity<DashboardActivity>()
			else
				launchActivity<RegisterActivity>()

			finish()
		}
	}
}
