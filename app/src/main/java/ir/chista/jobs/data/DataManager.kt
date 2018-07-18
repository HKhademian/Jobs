package ir.chista.jobs.data

import ir.chista.jobs.App
import ir.chista.jobs.data.api.ApiManager
import ir.chista.jobs.data.database.DatabaseRepository
import ir.chista.jobs.data.database.toEntity
import ir.chista.jobs.data.model.*
import ir.chista.util.ObservableData
import ir.chista.util.ObservableMutableList
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import ru.gildor.coroutines.retrofit.await

internal object DataManager {
  private val modeSubject = ObservableData(init = Mode.Offline)
  val mode get() = modeSubject.data
  val modeObservable = modeSubject.observable

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

    loadOnlineUserData()

    modeSubject.data = Mode.Online
  }

  /** update all cached data */
  suspend fun loadOfflineData() {
    AccountManager.refresh(false, false)

    loadOfflineUserData()

    modeSubject.data = Mode.Offline
  }

  /** update all cached data for user from api server and write on database */
  private suspend fun loadOnlineUserData() {
    // if (!AccountManager.user.isFresh) return

    val calls = arrayListOf<Deferred<*>>()

    calls += async {
      val items = ApiManager.jobs.list().await()
      this@DataManager.jobs.replace(items)
      DatabaseRepository.jobDao.clear()
      DatabaseRepository.jobDao.insertAll(items.toEntity())
    }

    calls += async {
      val items = ApiManager.skills.list().await()
      this@DataManager.skills.replace(items)
      DatabaseRepository.skillDao.clear()
      DatabaseRepository.skillDao.insertAll(items.toEntity())
    }

    calls += async {
      val items = ApiManager.requests.list(AccountManager.user.accessToken).await()
      this@DataManager.requests.replace(items)
      DatabaseRepository.requestDao.clear()
      DatabaseRepository.requestDao.insertAll(items.toEntity())
    }

    calls += async {
      val items = ApiManager.chats.list(AccountManager.user.accessToken).await()
      this@DataManager.chats.replace(items)
      DatabaseRepository.chatDao.clear()
      DatabaseRepository.chatDao.insertAll(items.toEntity())
    }

    calls += async {
      val items = ApiManager.matches.list(AccountManager.user.accessToken).await()
      this@DataManager.matches.replace(items)
      DatabaseRepository.matchDao.clear()
      DatabaseRepository.matchDao.insertAll(items.toEntity())
    }

    calls += async {
      val items = ApiManager.users.list(AccountManager.user.accessToken).await()
      this@DataManager.users.replace(items)
      DatabaseRepository.userDao.clear()
      DatabaseRepository.userDao.insertAll(items.toEntity())
    }

    calls.map { it.await() }
  }

  /** update all cached data for user from database */
  private suspend fun loadOfflineUserData() {
    val calls = arrayListOf<Deferred<*>>()

    calls += async {
      val items = DatabaseRepository.jobDao.list()
      this@DataManager.jobs.replace(items)
    }

    calls += async {
      val items = DatabaseRepository.skillDao.list()
      this@DataManager.skills.replace(items)
    }

    calls += async {
      val items = DatabaseRepository.chatDao.list()
      this@DataManager.chats.replace(items)
    }

    calls += async {
      val items = DatabaseRepository.requestDao.list()
      this@DataManager.requests.replace(items)
    }

    calls += async {
      val items = DatabaseRepository.matchDao.list()
      this@DataManager.matches.replace(items)
    }

    calls += async {
      val items = DatabaseRepository.userDao.list()
      this@DataManager.users.replace(items)
    }

    calls.map { it.await() }
  }

  enum class Mode { Online, Offline, Error }
}
