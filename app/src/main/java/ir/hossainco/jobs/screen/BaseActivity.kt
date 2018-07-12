package ir.hossainco.jobs.screen

import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA
import android.support.v7.app.AppCompatActivity
import ir.hossainco.jobs.util.LocaleManager.updateLocale

abstract class BaseActivity : AppCompatActivity() {
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
