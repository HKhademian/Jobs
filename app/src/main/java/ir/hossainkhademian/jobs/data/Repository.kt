/**
 * https://blog.gojekengineering.com/multi-threading-like-a-boss-in-android-with-rxjava-2-b8b7cf6eb5e2
 * > only One SubscriptionShoulder: first one near the source
 * > many ObservationShoulder: last near to subscriber
 * > observeOn can deliver data for every purposes to target threadPool
 *
 * RxJava2: for every rx styles queries like map, filter, ...
 * KotlinCoroutines: for ApiService and Network connections
 */
package ir.hossainkhademian.jobs.data

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.AccountManager.initAccountManager
import ir.hossainkhademian.jobs.data.DataManager.initDataManager
import ir.hossainkhademian.jobs.data.api.ApiManager
import ir.hossainkhademian.jobs.data.api.ApiManager.initApiManager
import ir.hossainkhademian.jobs.data.database.DatabaseRepository.initDatabaseManager
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.util.Observables
import ir.hossainkhademian.util.Observables.debounceAfter
import ir.hossainkhademian.util.Observables.throttleWithTimeoutAfter
import ru.gildor.coroutines.retrofit.await
import java.util.concurrent.TimeUnit

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
            .filterNot { it.key == AccountManager.id }
            .map { (contactId, contactChats) ->
              UserChat(users.getById(contactId), contactChats.last(), contactChats.filterUnseen().count())
            }
        }
        .observeOn(AndroidSchedulers.mainThread())
    }


    suspend fun send(contactId: ID, message: String) {
      if (!AccountManager.isLoggedIn && AccountManager.isFresh)
        throw RuntimeException("please login first")

      val items = ApiManager.chats.send(AccountManager.accessToken, contactId, message).await()
      if (items.isNotEmpty()) {
        val ids = items.mapId()
        DataManager.chats.merge(items) { ids.contains(it.id) }
      }
    }

    suspend fun seen(senderId: ID) {
      if (!AccountManager.isLoggedIn && AccountManager.isFresh)
        throw RuntimeException("please login first")

      val items = ApiManager.chats.markAsSeen(AccountManager.accessToken, senderId).await()
      if (items.isNotEmpty()) {
        val ids = items.mapId()
        DataManager.chats.merge(items) { ids.contains(it.id) }
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
        .getById(requestId)
        .observeOn(AndroidSchedulers.mainThread())
    }

    suspend fun refresh() {
      if (!AccountManager.isLoggedIn && AccountManager.isFresh)
        throw RuntimeException("please login first")

      //val items = ApiManager.requests.list(AccountManager.accessToken).await()
      //val ids = items.mapId()
      //DataManager.requests.merge(items) { ids.contains(it.id) }

      DataManager.loadOnlineUserData()
    }
  }
}
