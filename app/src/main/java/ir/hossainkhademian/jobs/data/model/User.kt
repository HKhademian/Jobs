package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json

private fun generateAvatarUrl(userId: ID): String {
  val i = userId.sumBy { it.toInt() } // userId.toInt() //
  val index = i % 50
  val gender = if (i % 2 == 0) "women" else "men"
  return "https://randomuser.me/api/portraits/$gender/$index.jpg"
}

val EmptyUser = UserData(id = emptyID, lastSeen = 0L)
val User.avatarUrl: String get() = generateAvatarUrl(id)
val User.isAdmin get() = role == UserRole.Admin
val User.isBroker get() = role == UserRole.Broker

interface User : IdModel {
  override val id: ID
  val title: String
  val lastSeen: Long
  val role: UserRole
}

enum class UserRole(val key: String) {
  User("user"), Broker("broker"), Admin("admin");

  companion object {
    fun from(role: String) = when (role.toLowerCase()) {
      Admin.key.toLowerCase() -> Admin
      Broker.key.toLowerCase() -> Broker
      else -> User
    }
  }
}

open class UserData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = "",
  @Json(name = "lastSeen") override val lastSeen: Long = System.currentTimeMillis(),
  @Json(name = "role") open val roleStr: String = UserRole.User.key
) : User {
  override val role get() = UserRole.from(roleStr)
}

fun User.toData() = when (this) {
  is UserData -> this
  is LoginData -> this
  else -> UserData(
    id = id,
    title = title,
    roleStr = role.key,
    lastSeen = lastSeen
  )
}

