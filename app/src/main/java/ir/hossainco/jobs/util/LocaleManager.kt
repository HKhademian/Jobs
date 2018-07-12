package ir.hossainco.jobs.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import com.chibatching.kotpref.KotprefModel
import java.util.*
import android.app.Activity


object LocaleManager {
  private const val DEFAULT = "fa-IR" //"en-US"
  private const val PREF_LOCALE = "app.locale" //"en-US"

  var Context.savedLocale
    get() = try {
      PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_LOCALE, DEFAULT)!!
    } catch (e: Exception) {
      DEFAULT
    }
    set(value) {
      PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PREF_LOCALE, value).apply()
    }

  private fun fromString(locale: String): Locale {
    val parts = locale.split("-")
    return Locale(parts[0], parts[1])
  }

  fun Context.updateLocale() =
      setLocale(fromString(savedLocale))

  fun Context.updateLocale(locale: Locale): Context {
    savedLocale = locale.toString()
    return setLocale(locale)
  }

  private fun Context.setLocale(locale: Locale): Context {
    Locale.setDefault(locale)
    val res = resources
    val config = Configuration(res.configuration)
    return if (Build.VERSION.SDK_INT >= 17) {
      config.setLocale(locale)
      createConfigurationContext(config)
    } else {
      config.locale = locale
      res.updateConfiguration(config, res.displayMetrics)
      this
    }
  }
}
