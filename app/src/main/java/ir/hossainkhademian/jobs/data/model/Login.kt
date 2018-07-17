package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json

val EmptyLogin = LoginData(id = emptyID, lastSeen = 0)

val Login.isGuest get() = refreshToken.isEmpty()
val Login.isLoggedIn get() = refreshToken.isNotEmpty()
val Login.isFresh get() = accessToken.isNotEmpty()

interface Login : User {
  override val id: ID
  override val title: String
  override val role: UserRole
  val phone: String
  val refreshToken: String
  val accessToken: String
}

interface LocalLogin : Login

class LoginData(
  id: ID = generateID,
  title: String = "",
  lastSeen: Long = System.currentTimeMillis(),
  roleStr: String = UserRole.User.key,
  @Json(name = "phone") override val phone: String = "",
  @Json(name = "refreshToken") override val refreshToken: String = "",
  @Json(name = "accessToken") override val accessToken: String = ""
) : UserData(id, title, lastSeen, roleStr), LocalLogin {
  override val role get() = UserRole.from(roleStr)
}

fun Login.toData() = when {
  this is LoginData -> this
  else -> LoginData(
    id = id,
    title = title,
    lastSeen = lastSeen,
    phone = phone,
    roleStr = role.key,
    accessToken = accessToken,
    refreshToken = refreshToken
  )
}

fun <T : Login> Iterable<T>.toData() =
  map { it.toData() }

fun Login.copy(
  id: ID? = null,
  title: String? = null,
  lastSeen: Long? = null,
  phone: String? = null,
  role: UserRole? = null,
  accessToken: String? = null,
  refreshToken: String? = null
) = LoginData(
  id = id ?: this.id,
  title = title ?: this.title,
  lastSeen = lastSeen ?: this.lastSeen,
  phone = phone ?: this.phone,
  roleStr = (role ?: this.role).key,
  accessToken = accessToken ?: this.accessToken,
  refreshToken = refreshToken ?: this.refreshToken
)
