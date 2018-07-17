package ir.hossainkhademian.jobs.data.api.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.model.*

data class ChatMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "senderId") val senderId: ID = emptyID,
  @Json(name = "receiverId") val receiverId: ID = emptyID,
  @Json(name = "messageField") val message: String = "",
  @Json(name = "unseen") var unseen: Boolean = true,
  @Json(name = "time") val time: Long = System.currentTimeMillis()
):IdModel

fun ChatMock.copy(
  id: ID? = null,
  senderId: ID? = null,
  receiverId: ID? = null,
  message: String? = null,
  time: Long? = null,
  unseen: Boolean? = null
) = ChatMock(
  id = id ?: this.id,
  senderId = senderId ?: this.senderId,
  receiverId = receiverId ?: this.receiverId,
  message = message ?: this.message,
  time = time ?: this.time,
  unseen = unseen ?: this.unseen
)

fun ChatMock.toData() = ChatData(
  id = id,
  senderId = senderId,
  receiverId = receiverId,
  message = message,
  time = time,
  unseen = unseen
)

fun Chat.toMock() = ChatMock(
  id = id,
  senderId = senderId,
  receiverId = receiverId,
  message = message,
  time = time,
  unseen = unseen
)

fun Iterable<ChatMock>.toData() = map { it.toData() }
fun <T : Chat> Iterable<T>.toMock() = map { it.toMock() }
