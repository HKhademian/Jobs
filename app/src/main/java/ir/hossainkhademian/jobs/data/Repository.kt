package ir.hossainkhademian.jobs.data

import io.reactivex.Observable
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.AccountManager.initAccountManager
import ir.hossainkhademian.jobs.data.DataManager.initDataManager
import ir.hossainkhademian.jobs.data.api.ApiManager
import ir.hossainkhademian.jobs.data.api.ApiManager.initApiManager
import ir.hossainkhademian.jobs.data.database.DatabaseRepository.initDatabaseManager
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.util.Observables
import ru.gildor.coroutines.retrofit.await

object Repository {
  val jobsObservable get() = DataManager.jobs.observable
  val jobsList: List<Job> get() = DataManager.jobs

  val skillsObservable get() = DataManager.skills.observable
  val skillsList: List<Skill> get() = DataManager.skills

  val usersObservable get() = DataManager.users.observable
  val usersList: List<User> get() = DataManager.users

  val requestsObservable get() = DataManager.requests.observable
  val requestsList: List<Request> get() = DataManager.requests

  val chatsObservable get() = DataManager.chats.observable
  val chatsList: List<Chat> get() = DataManager.chats

  fun App.initRepository() {
    initDataManager()
    initAccountManager()
    initDatabaseManager()
    initApiManager()
  }

  suspend fun chatSend(contactId: ID, message: String) {
    if (!AccountManager.isLoggedIn && AccountManager.isFresh)
      throw RuntimeException("please login first")

    val chats = ApiManager.chats.send(AccountManager.accessToken, contactId, message).await()
    DataManager.chats += chats  // .map { it.toMutable() }

    // val chats = ApiManager.chats.list(AccountManager.accessToken, contactId).await()
    // DataManager.chats.removeAll { chats.any { chat -> chat.id == it.id } }
    // DataManager.chats += chats
  }

  suspend fun chatSeen(chatId: ID) {
    if (!AccountManager.isLoggedIn && AccountManager.isFresh)
      throw RuntimeException("please login first")

    val chat = ApiManager.chats.markAsSeen(AccountManager.accessToken, chatId).await()

    // DataManager.chats.findById(chatId)?.unseen = false
    DataManager.chats.removeAll { it.id == chatId }
    DataManager.chats += chat
  }


  fun getChatsByContact(userId: ID): Observable<List<Chat>> =
    DataManager.chats.observable
      .map { chats ->
        chats
          .filterByContactId(userId)
          .sortedBy { it.time } // sort after filter for  speed
      }

  fun getUserChats() =
    Observables.combineLatest(DataManager.chats.observable, DataManager.users.observable) { chats: List<Chat>, users: List<User> -> chats to users }
      .map { (chats, users) ->
        chats
          .sortedByDescending { it.time } // sort before filter is a bit slower
          .groupBy { it.contactId }
          .filterNot { it.key == AccountManager.id }
          .map { (contactId, contactChats) ->
            users.getById(contactId) to contactChats.filterUnseen().count()
          }
        //chats
        //  .sortedByDescending { it.time } // sort before filter is a bit slower
        //  .distinctUserId(AccountManager.id)
        //  .map { userId ->
        //    users.getById(userId) to chats.filterByContactId(userId).count()
        //  }
      }!!
}
