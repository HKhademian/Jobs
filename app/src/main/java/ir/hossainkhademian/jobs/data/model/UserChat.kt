package ir.hossainkhademian.jobs.data.model

class UserChat(
  val user: LocalUser,
  val lastChat: LocalChat,
  val unreadChatCount: Int
)

val EmptyUserChat = UserChat(EmptyUser, EmptyChat, 0)
