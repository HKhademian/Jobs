package ir.chista.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import ir.chista.jobs.App
import java.util.*


object LocaleManager {
  private const val DEFAULT = "en-US" // "fa-IR" //
  private const val PREF_LOCALE = "app.locale"
  private var default = DEFAULT

  fun App.initLocalManager(default: String = DEFAULT) {
    this@LocaleManager.default = default
  }

  var Context.savedLocale
    get() = try {
      PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_LOCALE, default) ?: default
    } catch (e: Exception) {
      default
    }
    set(value) {
      PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PREF_LOCALE, value).apply()
    }

  private fun fromString(locale: String): Locale {
    val parts = locale.split("-", ",", " ", "_", ".", ":", "+")
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
