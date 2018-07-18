package ir.chista.jobs.data.model

import com.squareup.moshi.Json

private fun generateAvatarUrl(id: ID): String? {
  if (id.isEmpty) return null
  val i = id.sumBy { it.toInt() } // id.toInt() //
  val index = i % 50
  val gender = "men" // if (i % 2 == 0) "women" else "men"
  return "https://randomuser.me/api/portraits/$gender/$index.jpg"
}

//private fun generateAvatarUrl(id: ID): String {
//  return "https://api.adorable.io/avatars/256/id.png"
//}
//

val EmptyUser = UserData(id = emptyID, lastSeen = 0L)
val User.avatarUrl get() = generateAvatarUrl(id)
val User.isAdmin get() = role == UserRole.Admin
val User.isNotAdmin get() = role != UserRole.Admin
val User.isBroker get() = role == UserRole.Broker
val User.isNotBroker get() = role != UserRole.Broker
val User.isUser get() = role == UserRole.User
val User.isNotUser get() = role != UserRole.User

interface User : IdModel {
  override val id: ID
  val title: String
  val lastSeen: Long
  val role: UserRole
}

interface LocalUser : User

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
) : LocalUser {
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

fun <T : User> Iterable<T>.toData() = map { it.toData() }

fun <T : User> Iterable<T>.filterIsBroker() =
  filter { it.isBroker }

fun <T : User> Iterable<T>.filterNotIsBroker() =
  filterNot { it.isBroker }

fun <T : User> Iterable<T>.filterIsAdmin() =
  filter { it.isAdmin }

fun <T : User> Iterable<T>.filterNotIsAdmin() =
  filterNot { it.isAdmin }

fun <T : User> Iterable<T>.filterIsUser() =
  filter { it.isUser }

fun <T : User> Iterable<T>.filterNotIsUser() =
  filterNot { it.isUser }
