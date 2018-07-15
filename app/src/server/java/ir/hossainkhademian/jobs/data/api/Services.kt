package ir.hossainkhademian.jobs.data.api

import android.content.Context
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.BuildConfig
import ir.hossainkhademian.util.Interceptors
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

internal object Services {
  private const val CACHE_DIR = "api-cache"
  private const val CACHE_SIZE = 64L * 1024 * 1024
  private const val CONNECTION_TIMEOUT = 5L
  private const val BASE_URL = BuildConfig.HOST // "http://localhost:3000/" /* using adb reverse */ //

  lateinit var account: AccountService; private set
  lateinit var skill: SkillService; private set
  lateinit var job: JobService; private set
  lateinit var request: RequestService; private set
  lateinit var chat: ChatService; private set
  lateinit var user: UserService; private set

  private fun create(retrofit: Retrofit) {
    account = retrofit.create(AccountService::class.java)
    skill = retrofit.create(SkillService::class.java)
    job = retrofit.create(JobService::class.java)
    request = retrofit.create(RequestService::class.java)
    chat = retrofit.create(ChatService::class.java)
    user = retrofit.create(UserService::class.java)
  }

  fun App.initServices() {
    val context = applicationContext
    val cacheDir = File(context.cacheDir, CACHE_DIR)
    val cache = Cache(cacheDir, CACHE_SIZE)
    val moshiConverterFactory = MoshiConverterFactory.create()
    // val liveDataAdapterFactory = LiveDataCallAdapterFactory()
    // val rx2AdapterFactory = RxJava2CallAdapterFactory.create()//Async()

    val client = OkHttpClient.Builder()
      .cache(cache)
      .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
      .addInterceptor(Interceptors.CacheResponseInterceptor())
      .addNetworkInterceptor(Interceptors.CacheResponseNetworkInterceptor())
      .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
      // .addNetworkInterceptor(ChuckInterceptor(context))
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(client)
      .addConverterFactory(moshiConverterFactory)
      // .addCallAdapterFactory(liveDataAdapterFactory)
      // .addCallAdapterFactory(rx2AdapterFactory)
      .build()

    create(retrofit)
  }
}
