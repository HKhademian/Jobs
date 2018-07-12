package ir.hossainco.jobs.data.api

import ir.hossainco.jobs.App
import ir.hossainkhademian.myarch.util.Interceptors
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiRepository {
  private const val CACHE_DIR = "api-cache"
  private const val CACHE_SIZE = 64L * 1024 * 1024
  private const val CONNECTION_TIMEOUT = 5L
  private const val BASE_URL = "http://localhost:3000" /* using adb reverse */

  private lateinit var accountService: AccountService

  fun App.initApiManager() {
    val context = applicationContext
    val cacheDir = File(context.cacheDir, CACHE_DIR)
    val cache = Cache(cacheDir, CACHE_SIZE)
    val moshiConverterFactory = MoshiConverterFactory.create()
    // val liveDataAdapterFactory = LiveDataCallAdapterFactory()
    val rx2AdapterFactory = RxJava2CallAdapterFactory.create()//Async()

    val client = OkHttpClient.Builder()
        .cache(cache)
        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(Interceptors.CacheResponseInterceptor())
        .addNetworkInterceptor(Interceptors.CacheResponseNetworkInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(moshiConverterFactory)
        // .addCallAdapterFactory(liveDataAdapterFactory)
        .addCallAdapterFactory(rx2AdapterFactory)
        .build()

    accountService = retrofit.create(AccountService::class.java)
  }

}
