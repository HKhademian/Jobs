package ir.hossainco.jobs.data.model

fun generateAvatarUrl(userId: ID): String {
  val i = userId.last().toInt()
  val index = (i % 100) / 2
  val gender = if (i % 2 == 0) "women" else "men"
  return """https://randomuser.me/api/portraits/$gender/$index.jpg"""
}

val User.avatarUrl: String get() = generateAvatarUrl(id)

interface User {
  enum class Role(val key: String) {
    User("user"), Broker("broker"), Admin("admin");

    companion object {
      fun from(role: String) = when (role.toLowerCase()) {
        Admin.key.toLowerCase() -> Admin
        Broker.key.toLowerCase() -> Broker
        else -> User
      }
    }
  }

  val id: ID
  val title: String
  val lastSeen: Long
  val role: Role

  val isAdmin get() = role == Role.Admin
  val isBroker get() = role == Role.Broker
}

interface UserWithChats {
  val user: User
  val chats: List<Chat>
}

interface UserRequests {
  val user: User
  val requests: List<Request>
}


