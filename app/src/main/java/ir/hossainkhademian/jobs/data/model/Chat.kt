package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.data.DataManager

val EmptyChat = ChatData(id = emptyID, unseen = false, time = 0)

interface Chat : IdModel {
  override val id: ID
  val senderId: ID
  val receiverId: ID
  val message: String
  val unseen: Boolean
  val time: Long

  val seen: Boolean

  val direction get() = getDirection(AccountManager.id)
  val sender get() = DataManager.users.findById(senderId) ?: EmptyUser
  val receiver get() = DataManager.users.findById(receiverId) ?: EmptyUser

}

fun Chat.getDirection(userId: ID) = when (userId) {
  senderId -> ChatDirection.SEND
  receiverId -> ChatDirection.RECEIVE
  else -> ChatDirection.SEND  // throw RuntimeException("bad value")
}

fun Chat.getContactId(userId: ID) = when (userId) {
  senderId -> receiverId
  receiverId -> senderId
  else -> receiverId  // throw RuntimeException("bad value")
}

fun Chat.isSended(userId: ID) = getDirection(userId) == ChatDirection.SEND
fun Chat.isReceived(userId: ID) = getDirection(userId) == ChatDirection.RECEIVE
val Chat.isSended get() = isSended(AccountManager.id)
val Chat.isReceived get() = isReceived(AccountManager.id)
val Chat.contactId get() = getContactId(AccountManager.id)

enum class ChatDirection(val key: String) {
  SEND("send"), RECEIVE("receive");

  companion object {
    fun from(direction: String) = when (direction.toLowerCase()) {
      SEND.key.toLowerCase() -> SEND
      RECEIVE.key.toLowerCase() -> RECEIVE
      else -> SEND // throw RuntimeException("bad value")
    }
  }
}

open class ChatData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "senderId") override val senderId: ID = emptyID,
  @Json(name = "receiverId") override val receiverId: ID = emptyID,
  @Json(name = "messageField") override val message: String = "",
  @Json(name = "unseen") override var unseen: Boolean = true,
  @Json(name = "time") override val time: Long = System.currentTimeMillis()
) : Chat {
  @Suppress("LeakingThis")
  override val seen: Boolean = !unseen

}

fun Chat.toData() = when (this) {
  is ChatData -> this
  else -> ChatData(
    id = id,
    senderId = senderId,
    receiverId = receiverId,
    message = message,
    time = time,
    unseen = unseen
  )
}

fun Chat.copy(
  id: ID? = null,
  senderId: ID? = null,
  receiverId: ID? = null,
  message: String? = null,
  time: Long? = null,
  unseen: Boolean? = null
) = ChatData(
  id = id ?: this.id,
  senderId = senderId ?: this.senderId,
  receiverId = receiverId ?: this.receiverId,
  message = message ?: this.message,
  time = time ?: this.time,
  unseen = unseen ?: this.unseen
)

fun <T : Chat> Iterable<T>.toData() = map { it.toData() }

fun <T : Chat> Iterable<T>.filterBySenderId(senderId: ID) =
  filter { it.senderId == senderId }

fun <T : Chat> Iterable<T>.filterByReceiverId(receiverId: ID) =
  filter { it.receiverId == receiverId }

fun <T : Chat> Iterable<T>.filterByContactId(contactId: ID) =
  filter { it.senderId == contactId || it.receiverId == contactId }

fun <T : Chat> Iterable<T>.filterByUserContactId(userId: ID, contactId: ID) =
  filter { (it.senderId == userId && it.receiverId == contactId) || (it.senderId == contactId && it.receiverId == userId) }

fun <T : Chat> Iterable<T>.filterUnseen() =
  filter { it.isReceived && it.unseen }

//fun <T : Chat> Iterable<T>.filterSeen() =
//  filter { it.isSended || if (it is ChatData) it.seen == true else !it.unseen }
//fun <T : Chat> Iterable<T>.filterNotSeen() =
//  filterNot { it.isSended || if (it is ChatData) it.seen == true else !it.unseen }

fun <T : Chat> Iterable<T>.distinctUserId() =
  map { it.senderId }
    .union(map { it.receiverId })
    .distinct()

fun <T : Chat> Iterable<T>.distinctUserId(userId: ID) =
  map { it.senderId }
    .union(map { it.receiverId })
    .filterNot { it == userId }
    .distinct()
