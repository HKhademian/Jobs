/**
 * Multi dex: https://developer.android.com/studio/build/multidex
 * for method count reach the max! in one dex file
 */
package ir.chista.jobs

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.support.multidex.MultiDex
import com.chibatching.kotpref.Kotpref
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
//import io.github.inflationx.calligraphy3.CalligraphyConfig
//import io.github.inflationx.calligraphy3.CalligraphyInterceptor
//import io.github.inflationx.viewpump.ViewPump
//import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.chista.jobs.data.Repository.initRepository
import ir.chista.util.LocaleManager.initLocalManager
import ir.chista.util.LocaleManager.updateLocale
import net.danlew.android.joda.JodaTimeAndroid

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this))
      return
    LeakCanary.install(this)

    Kotpref.init(applicationContext)

    JodaTimeAndroid.init(applicationContext)

    Picasso.setSingletonInstance(Picasso.Builder(applicationContext)
      .loggingEnabled(true)
      .build())

//    ViewPump.init(ViewPump.builder()
//      .addInterceptor(CalligraphyInterceptor(
//        CalligraphyConfig.Builder()
//          .setDefaultFontPath("fonts/iransans.ttf")
//          .setFontAttrId(R.attr.fontPath)
//          .build()
//      )).build())

    initLocalManager("en-US")//("fa-IR")
    initRepository()
  }

  override fun attachBaseContext(base: Context) {
    var context = base
    context = context.updateLocale() // load saved locale
//    context = ViewPumpContextWrapper.wrap(context) // ViewPump view injector
    super.attachBaseContext(context)
    MultiDex.install(this)
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    super.onConfigurationChanged(newConfig)
    updateLocale()
  }
}
