package ir.hossainkhademian.jobs.data.api.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.model.*

class LoginMock(
  id: ID = generateID,
  title: String = "",
  lastSeen: Long = System.currentTimeMillis(),
  roleStr: String = UserRole.User.key,
  @Json(name = "phone") override val phone: String = "",
  @Json(name = "refreshToken") override val refreshToken: String = "",
  @Json(name = "accessToken") override val accessToken: String = ""
) : UserMock(id, title, lastSeen, roleStr), Login

fun LoginMock.copy(
  id: ID? = null,
  title: String? = null,
  lastSeen: Long? = null,
  phone: String? = null,
  role: UserRole? = null,
  accessToken: String? = null,
  refreshToken: String? = null
) = LoginMock(
  id = id ?: this.id,
  title = title ?: this.title,
  lastSeen = lastSeen ?: this.lastSeen,
  phone = phone ?: this.phone,
  roleStr = (role ?: this.role).key,
  accessToken = accessToken ?: this.accessToken,
  refreshToken = refreshToken ?: this.refreshToken
)

fun Login.toMock() = LoginMock(
  id = id,
  title = title,
  lastSeen = lastSeen,
  phone = phone,
  roleStr = role.key,
  accessToken = accessToken,
  refreshToken = refreshToken
)

fun LoginMock.toData() = LoginData(
  id = id,
  title = title,
  lastSeen = lastSeen,
  phone = phone,
  roleStr = role.key,
  accessToken = accessToken,
  refreshToken = refreshToken
)

fun Iterable<LoginMock>.toData() = map { it.toData() }
fun <T : Login> Iterable<T>.toMock() = map { it.toMock() }

