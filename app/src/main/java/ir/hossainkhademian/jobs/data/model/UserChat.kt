package ir.hossainkhademian.jobs.data.model

class UserChat(
  val user: User,
  val lastChat: Chat,
  val unreadChatCount: Int
)

val EmptyUserChat = UserChat(EmptyUser, EmptyChat, 0)
