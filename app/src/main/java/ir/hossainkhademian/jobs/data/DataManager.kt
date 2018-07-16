package ir.hossainkhademian.jobs.data

import android.content.Context
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.api.ApiManager
import ir.hossainkhademian.jobs.data.model.*
//import ir.hossainkhademian.jobs.data.model.toMutable
import ir.hossainkhademian.util.ObservableMutableList
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import ru.gildor.coroutines.retrofit.await

internal object DataManager {
  var mode: Mode = Mode.Offline
  internal val jobs = ObservableMutableList<Job>()
  internal val skills = ObservableMutableList<Skill>()
  internal val users = ObservableMutableList<User>()
  internal val requests = ObservableMutableList<Request>()
  internal val chats = ObservableMutableList<Chat>()
  internal val matches = ObservableMutableList<Match>()

  internal fun App.initDataManager() {
  }

  /** update all cached data */
  suspend fun loadOnlineData(context: Context) {
    AccountManager.refresh(context, false, false)

    loadOnlineStaticData(context)
    loadOnlineUserData(context)

    mode = Mode.Online
  }

  /** update all cached data for user */
  suspend fun loadOnlineUserData(context: Context) {
    if (!AccountManager.isFresh) return

    val calls = arrayListOf<Deferred<*>>()
    var requests = emptyList<Request>()
    var chats = emptyList<Chat>() // emptyList<MutableChatData>()
    var matches = emptyList<Match>() // emptyList<MutableChatData>()

    calls += async {
      requests = ApiManager.requests.list(AccountManager.accessToken).await()
    }

    calls += async {
      chats = ApiManager.chats.list(AccountManager.accessToken).await()
    }

    calls += async {
      matches = ApiManager.matches.list(AccountManager.accessToken).await()
    }

    calls.map { it.await() }

    val users = chats
      .distinctUserId()
      .let { userIds -> ApiManager.users.list(userIds).await() }


    // All done, now set & save data
    //launch(UI) {
    this@DataManager.requests.replace(requests)
    this@DataManager.chats.replace(chats)
    this@DataManager.matches.replace(matches)
    this@DataManager.users.replace(users)
    //}
    mode = Mode.Online
  }

  /** update all cached data for static */
  suspend fun loadOnlineStaticData(context: Context) {
    val calls = arrayListOf<Deferred<*>>()
    var jobs = emptyList<Job>()
    var skills = emptyList<Skill>()

    calls += async {
      jobs = ApiManager.jobs.list().await()
    }

    calls += async {
      skills = ApiManager.skills.list().await()
    }

    calls.map { it.await() }

    // All done, now set & save data
    //launch(UI) {
    this@DataManager.jobs.replace(jobs)
    this@DataManager.skills.replace(skills)
    //}
  }

  enum class Mode { Online, Offline, Error }
}
