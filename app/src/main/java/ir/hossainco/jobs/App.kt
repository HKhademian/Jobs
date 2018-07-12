package ir.hossainco.jobs

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.chibatching.kotpref.Kotpref
import com.squareup.leakcanary.LeakCanary
import ir.hossainco.jobs.data.AccountManager.initAccountManager
import ir.hossainco.jobs.data.DataManager.initDataManager
import ir.hossainco.jobs.util.LocaleManager.updateLocale

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this))
      return
    LeakCanary.install(this)

    Kotpref.init(applicationContext)
    initDataManager()
  }

  override fun attachBaseContext(base: Context) {
   super.attachBaseContext(base.updateLocale())
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    super.onConfigurationChanged(newConfig)
    updateLocale()
  }
}
