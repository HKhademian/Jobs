package ir.hossainkhademian.jobs.data.model

class UserChat(
  val user: LocalUser,
  val lastChat: LocalChat,
  val badge: String,
  val time: Long
)

val EmptyUserChat = UserChat(EmptyUser, EmptyChat, "", System.currentTimeMillis())
