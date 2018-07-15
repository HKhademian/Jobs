package ir.hossainkhademian.jobs.data.model

interface UserWithChats {
  val user: User
  val chats: List<Chat>

  val count: Int get() = chats.size
}
