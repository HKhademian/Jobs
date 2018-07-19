/**
 * https://blog.gojekengineering.com/multi-threading-like-a-boss-in-android-with-rxjava-2-b8b7cf6eb5e2
 * > only One SubscriptionShoulder: first one near the source
 * > many ObservationShoulder: last near to subscriber
 * > observeOn can deliver data for every purposes to target threadPool
 *
 * RxJava2: for every rx styles queries like map, filter, ...
 * KotlinCoroutines: for ApiService and Network connections
 */
package ir.chista.jobs.data

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.chista.jobs.App
import ir.chista.jobs.data.AccountManager.initAccountManager
import ir.chista.jobs.data.DataManager.initDataManager
import ir.chista.jobs.data.api.ApiManager
import ir.chista.jobs.data.api.ApiManager.initApiManager
import ir.chista.jobs.data.database.DatabaseManager
import ir.chista.jobs.data.database.DatabaseManager.initDatabaseManager
import ir.chista.jobs.data.database.toEntity
import ir.chista.jobs.data.model.*
import ir.chista.util.Observables
import ru.gildor.coroutines.retrofit.await
import java.io.IOException

object Repository {
  fun App.initRepository() {
    initDataManager()
    initAccountManager()
    initDatabaseManager()
    initApiManager()
  }

  object Jobs {
    fun list(): Observable<List<LocalJob>> {
      return DataManager.jobs.observable
        // .subscribeOn(Schedulers.io())
        //.debounceAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { it.sortedBy { it.title } }
        .observeOn(AndroidSchedulers.mainThread())
    }
  }

  object Skills {
    fun list(): Observable<List<LocalSkill>> {
      return DataManager.skills.observable
        // .subscribeOn(Schedulers.io())
        //.debounceAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { it.sortedBy { it.title } }
        .observeOn(AndroidSchedulers.mainThread()) // because we use memory storage (DataManager) it is useless but error reduces on future
    }
  }

  object Chats {
    fun getContact(userId: ID): Observable<LocalUser> {
      return DataManager.users.observable
        // .subscribeOn(Schedulers.io())
        //.debounceAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { users ->
          users.findById(userId, EmptyUser)
        }
        .observeOn(AndroidSchedulers.mainThread())
    }

    fun listsByContact(userId: ID): Observable<List<LocalChat>> {
      return DataManager.chats.observable
        // .subscribeOn(Schedulers.io())
        //.debounceAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { chats ->
          chats
            .filterByContactId(userId)
            .sortedBy { it.time } // sort after filter for  speed
        }
        .observeOn(AndroidSchedulers.mainThread())
    }

    fun listUserChats(): Observable<List<UserChat>> {
      return Observables.combineLatest(DataManager.chats.observable, DataManager.users.observable) { chats, users -> chats to users }
        // .subscribeOn(Schedulers.io())
        //.throttleWithTimeoutAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { (chats, users) ->
          chats
            .sortedByDescending { it.time } // sort before filter is a bit slower
            .groupBy { it.contactId }
            .filterNot { it.key == AccountManager.user.id }
            .map { (contactId, contactChats) ->
              val lastChat = contactChats.first()
              val unseenCount = contactChats.filterUnseen().count()

              val badge = if (unseenCount > 0) "$unseenCount" else ""
              val time = lastChat.time

              UserChat(users.getById(contactId), lastChat, badge, time)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())
    }


    suspend fun send(contactId: ID, message: String) {
      if (DataManager.mode == DataManager.Mode.Offline)
        throw IOException("you are in offline mode")
      if (!AccountManager.user.isLoggedIn && AccountManager.user.isFresh)
        throw IOException("please login first")

      val items = ApiManager.chats.send(AccountManager.user.accessToken, contactId, message).await()
      if (items.isNotEmpty()) {
        val ids = items.mapId()
        DataManager.chats.merge(items) { ids.contains(it.id) }
        DatabaseManager.chatDao.insertAll(items.toEntity())
      }
    }

    suspend fun seen(senderId: ID) {
      if (DataManager.mode == DataManager.Mode.Offline)
        throw IOException("you are in offline mode")
      if (!AccountManager.user.isLoggedIn && AccountManager.user.isFresh)
        throw IOException("please login first")

      val items = ApiManager.chats.markAsSeen(AccountManager.user.accessToken, senderId).await()
      if (items.isNotEmpty()) {
        val ids = items.mapId()
        DataManager.chats.merge(items) { ids.contains(it.id) }
        DatabaseManager.chatDao.insertAll(items.toEntity())
      }
    }
  }

  object Requests {
    fun list(): Observable<List<LocalRequest>> {
      return DataManager.requests.observable
        // .subscribeOn(Schedulers.io())
        //.debounceAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { requests -> requests.sortedBy { request -> request.time } }
        .observeOn(AndroidSchedulers.mainThread())
    }

    fun findById(requestId: ID): Observable<LocalRequest> {
      return DataManager.requests.observable
        // .subscribeOn(Schedulers.io())
        .take(1)
        .observeOn(Schedulers.computation())
        .findById(requestId, EmptyRequest)
        .observeOn(AndroidSchedulers.mainThread())
    }

    suspend fun refresh() {
      if (DataManager.mode == DataManager.Mode.Offline)
        throw IOException("you are in offline mode")
      if (!AccountManager.user.isLoggedIn && AccountManager.user.isFresh)
        throw IOException("please login first")

      //val items = ApiManager.requests.list(AccountManager.accessToken).await()
      //val ids = items.mapId()
      //DataManager.requests.merge(items) { ids.contains(it.id) }

      DataManager.loadOnlineData()
    }

    suspend fun update(requestId: ID, type: RequestType, jobId: ID, skillIds: List<ID>, detail: String): LocalRequest {
      if (DataManager.mode == DataManager.Mode.Offline)
        throw IOException("you are in offline mode")
      if (!AccountManager.user.isLoggedIn && AccountManager.user.isFresh)
        throw IOException("please login first")

      val isNew = requestId.isEmpty

      val request = if (isNew)
        ApiManager.requests.create(AccountManager.user.accessToken, type.key, jobId, skillIds, detail).await()
      else
        ApiManager.requests.edit(AccountManager.user.accessToken, requestId, type.key, jobId, skillIds, detail).await()

      DataManager.requests.merge(request) { it.id == request.id }
      DatabaseManager.requestDao.insertAll(request.toEntity())
      return request
    }

    suspend fun removeBroker(requestId: ID, brokerId: ID) {
      if (DataManager.mode == DataManager.Mode.Offline)
        throw IOException("you are in offline mode")
      if (!AccountManager.user.isLoggedIn && AccountManager.user.isFresh)
        throw IOException("please login first")

      if (AccountManager.user.isNotAdmin)
        throw RuntimeException("only admins can remove brokers from requests")

      val item = ApiManager.requests.removeBroker(AccountManager.user.accessToken, requestId, brokerId).await()
      DataManager.requests.merge(item) { it.id == item.id }
      DatabaseManager.requestDao.insert(item.toEntity())
    }

    suspend fun addBroker(requestId: ID, brokerId: ID) {
      if (DataManager.mode == DataManager.Mode.Offline)
        throw IOException("you are in offline mode")
      if (!AccountManager.user.isLoggedIn && AccountManager.user.isFresh)
        throw IOException("please login first")
      if (AccountManager.user.isNotAdmin)
        throw RuntimeException("only admins can remove brokers from requests")

      val item = ApiManager.requests.addBroker(AccountManager.user.accessToken, requestId, brokerId).await()
      DataManager.requests.merge(item) { it.id == item.id }
      DatabaseManager.requestDao.insert(item.toEntity())
    }
  }

  object Users {
    fun listBrokers(): Observable<List<LocalUser>> {
      return DataManager.users.observable
        // .subscribeOn(Schedulers.io())
        //.debounceAfter(1, 5, TimeUnit.SECONDS)
        .observeOn(Schedulers.computation())
        .map { users -> users.filterIsBroker() }
        .observeOn(AndroidSchedulers.mainThread())
    }
  }
}