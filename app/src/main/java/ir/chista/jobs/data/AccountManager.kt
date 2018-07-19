package ir.chista.jobs.data

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import io.reactivex.subjects.BehaviorSubject
import ir.chista.jobs.App
import ir.chista.jobs.data.api.ApiManager
import ir.chista.jobs.data.model.*
import ru.gildor.coroutines.retrofit.await

object AccountManager {
  /** this MockApiStorage must be replaced with a secure one */
  private object Storage : KotprefModel(), LocalLogin {
    override var id by stringPref()
    override var title by stringPref()
    override var role by enumValuePref(UserRole.User)
    override val lastSeen get() = System.currentTimeMillis()
    override var phone by stringPref()
    override var refreshToken by stringPref()
    override var accessToken = ""
  }

  object Taps : KotprefModel() {
    var navigationDone by booleanPref(false)
    var dashboardDone by booleanPref(false)
    var requestListDone by booleanPref(false)
  }

  val user = object : LocalLogin by Storage {}
  private val subject = BehaviorSubject.create<LocalLogin>()
  val observable = subject.hide()

  internal fun App.initAccountManager() {
    //val context = applicationContext
  }


  fun save(login: Login) = Storage.run {
    id = login.id
    title = login.title
    role = login.role
    phone = login.phone
    // lastSeen = lastSeen
    refreshToken = login.refreshToken
    accessToken = login.accessToken

    subject.onNext(user)

    Unit
  }

  /**
   * get new access token from current login (update token)
   */
  suspend fun refresh(force: Boolean = true, loadData: Boolean = true) {
    if (!user.isLoggedIn) return
    if (!force && user.isFresh) return
    val login = ApiManager.accounts.refresh(user.refreshToken).await()
    save(login)
    if (loadData) DataManager.loadOnlineData()
  }

  /**
   * exit from current session
   * maybe call server later
   */
  suspend fun logout() {
    Storage.clear()
    DataManager.chats.clear()
    DataManager.users.clear()
    DataManager.requests.clear()
    subject.onNext(user)
  }

  suspend fun login(phone: String, password: String) {
    val login = ApiManager.accounts.login(phone, password).await()
    save(login)
    // if (loadData) DataManager.loadOnlineUserData(context)
  }

  suspend fun register(phone: String, title: String) {
    val login = ApiManager.accounts.register(phone, title).await()
    save(login)
    // if (loadData) DataManager.loadOnlineUserData(context)
  }

  suspend fun changeTitle(title: String) {
    val login = ApiManager.accounts.changeTitle(user.refreshToken, title).await()
    save(login)
  }


  fun isPhoneValid(phone: String): Boolean =
    phone.matches(Regex("""\+98[0-9]{10}"""))

  fun isPasswordValid(password: String): Boolean =
    true
}
