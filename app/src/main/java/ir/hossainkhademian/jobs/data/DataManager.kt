package ir.hossainkhademian.jobs.data

import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.api.ApiManager
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.util.ObservableMutableList
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import ru.gildor.coroutines.retrofit.await

internal object DataManager {
  var mode: Mode = Mode.Offline
  internal val jobs = ObservableMutableList<LocalJob>()
  internal val skills = ObservableMutableList<LocalSkill>()
  internal val users = ObservableMutableList<LocalUser>()
  internal val requests = ObservableMutableList<LocalRequest>()
  internal val chats = ObservableMutableList<LocalChat>()
  internal val matches = ObservableMutableList<LocalMatch>()

  internal fun App.initDataManager() {
  }

  /** update all cached data */
  suspend fun loadOnlineData() {
    AccountManager.refresh(false, false)

    loadOnlineStaticData()
    loadOnlineUserData()

    mode = Mode.Online
  }

  /** update all cached data for user */
  suspend fun loadOnlineUserData() {
    if (!AccountManager.user.isFresh) return

    val calls = arrayListOf<Deferred<*>>()
    var requests = emptyList<RequestData>()
    var chats = emptyList<ChatData>()
    var matches = emptyList<MatchData>()
    var users = emptyList<UserData>()

    calls += async {
      requests = ApiManager.requests.list(AccountManager.user.accessToken).await()
    }

    calls += async {
      chats = ApiManager.chats.list(AccountManager.user.accessToken).await()
    }

    calls += async {
      matches = ApiManager.matches.list(AccountManager.user.accessToken).await()
    }

    calls += async {
      users = ApiManager.users.list(AccountManager.user.accessToken).await()
    }

    calls.map { it.await() }


    // All done, now set & save data
    this@DataManager.requests.replace(requests)
    this@DataManager.chats.replace(chats)
    this@DataManager.matches.replace(matches)
    this@DataManager.users.replace(users)

    mode = Mode.Online
  }

  /** update all cached data for static */
  suspend fun loadOnlineStaticData() {
    val calls = arrayListOf<Deferred<*>>()
    var jobs = emptyList<JobData>()
    var skills = emptyList<SkillData>()

    calls += async {
      jobs = ApiManager.jobs.list().await()
    }

    calls += async {
      skills = ApiManager.skills.list().await()
    }

    calls.map { it.await() }

    // All done, now set & save data
    this@DataManager.jobs.replace(jobs)
    this@DataManager.skills.replace(skills)
  }

  enum class Mode { Online, Offline, Error }
}
