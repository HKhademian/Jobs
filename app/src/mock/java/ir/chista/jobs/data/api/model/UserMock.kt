package ir.chista.jobs.data.api.model

import com.squareup.moshi.Json
import ir.chista.jobs.data.model.*


open class UserMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = "",
  @Json(name = "lastSeen") override val lastSeen: Long = System.currentTimeMillis(),
  @Json(name = "role") val roleStr: String = UserRole.User.key
) : User {
  override val role get() = UserRole.from(roleStr)
}

fun UserMock.copy(
  id: ID? = null,
  title: String? = null,
  lastSeen: Long? = null,
  roleStr: String? = null
) = UserMock(
  id = id ?: this.id,
  title = title ?: this.title,
  lastSeen = lastSeen ?: this.lastSeen,
  roleStr = roleStr ?: this.roleStr
)

fun UserData.toData() = UserData(
  id = id,
  title = title,
  roleStr = role.key,
  lastSeen = lastSeen
)

fun User.toMock() = UserMock(
  id = id,
  title = title,
  roleStr = role.key,
  lastSeen = lastSeen
)

fun Iterable<UserData>.toData() = map { it.toData() }
fun <T : User> Iterable<T>.toMock() = map { it.toMock() }
