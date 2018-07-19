package ir.chista.jobs.util

import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ir.chista.util.LocaleManager.updateLocale

abstract class BaseActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    super.onCreate(savedInstanceState)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base.updateLocale())
    resetTitle()
  }

  private fun resetTitle() = try {
    val label = packageManager.getActivityInfo(componentName, GET_META_DATA).labelRes
    if (label != 0)
      setTitle(label)
    else Unit
  } catch (e: Exception) {
    e.printStackTrace()
  }
}
