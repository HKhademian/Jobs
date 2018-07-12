package ir.hossainco.jobs.data

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import ir.hossainco.jobs.App
import ir.hossainco.jobs.data.model.User

object AccountManager {
  private object LoginData : KotprefModel() {
    var token by stringPref(default = "")
    var title by stringPref()
    var phone by stringPref()
    var type by enumValuePref(User.Role.User)
  }

  val title get() = LoginData.title
  val phone get() = LoginData.phone
  val type get() = LoginData.type

  val isGuest get() = LoginData.token.isEmpty()
  val isLoggedIn get() = LoginData.token.isNotEmpty()
  val isAdmin get() = LoginData.type == User.Role.Admin
  val isBroker get() = LoginData.type == User.Role.Broker

  fun App.initAccountManager() {
    val context = applicationContext
  }

  fun login(phone: String, password: String): Boolean {
    Thread.sleep(2000)
    if (phone.isEmpty() || !isPhoneValid(phone) || phone.takeLast(1) != password)
      return false
    LoginData.phone = phone
    return true
  }

  fun logout() {
    Thread.sleep(2000)
    LoginData.clear()
  }

  fun register(phone: String): Boolean {
    Thread.sleep(2000)
    val last = phone.takeLast(1)
    return isPhoneValid(phone) && last != "0" && last != "1"
  }


  fun isPhoneValid(phone: String): Boolean =
      phone.matches(Regex("""\+98[0-9]{10}"""))

  fun isPasswordValid(password: String): Boolean =
      true

}
