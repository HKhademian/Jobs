package ir.hossainkhademian.jobs.data

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.api.ApiManager
import ir.hossainkhademian.jobs.data.model.Login
import ir.hossainkhademian.jobs.data.model.UserRole
import ir.hossainkhademian.jobs.data.model.isFresh
import ir.hossainkhademian.jobs.data.model.isLoggedIn
import ru.gildor.coroutines.retrofit.await

object AccountManager : Login by Storage {
  /** this MockApiStorage must be replaced with a secure one */
  private object Storage : KotprefModel(), Login {
    override var id by stringPref()
    override var title by stringPref()
    override var role by enumValuePref(UserRole.User)
    override val lastSeen get() = System.currentTimeMillis()
    override var phone by stringPref()
    override var refreshToken by stringPref()
    override var accessToken = ""
  }

  internal fun App.initAccountManager() {
    //val context = applicationContext
  }


  fun save(context: Context, login: Login) = Storage.run {
    id = login.id
    title = login.title
    role = login.role
    phone = login.phone
    // lastSeen = lastSeen
    refreshToken = login.refreshToken
    accessToken = login.accessToken

    Unit
  }

  /**
   * get new access token from current login (update token)
   */
  suspend fun refresh(context: Context, force: Boolean = true, loadData: Boolean = true) {
    if (!isLoggedIn) return
    if (!force && isFresh) return
    val login = ApiManager.accounts.refresh(refreshToken).await()
    save(context, login)
    //delay(2000)
    if (loadData) DataManager.loadOnlineUserData(context)
  }

  /**
   * exit from current session
   * maybe call server later
   */
  suspend fun logout(context: Context) {
    Storage.clear()
    DataManager.chats.clear()
    DataManager.users.clear()
    DataManager.requests.clear()
    //delay(1000)
  }

  suspend fun login(context: Context, phone: String, password: String, loadData: Boolean = true) {
    val login = ApiManager.accounts.login(phone, password).await()
    save(context, login)
    if (loadData) DataManager.loadOnlineUserData(context)
  }

  suspend fun register(context: Context, phone: String, loadData: Boolean = true) {
    val login = ApiManager.accounts.register(phone).await()
    save(context, login)
    if (loadData) DataManager.loadOnlineUserData(context)
  }


  fun isPhoneValid(phone: String): Boolean =
    phone.matches(Regex("""\+98[0-9]{10}"""))

  fun isPasswordValid(password: String): Boolean =
    true
}
