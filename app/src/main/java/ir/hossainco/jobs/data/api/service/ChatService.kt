@file:Suppress("PackageDirectoryMismatch")

package ir.hossainco.jobs.data.api

import com.squareup.moshi.Json
import ir.hossainco.jobs.data.model.Chat
import ir.hossainco.jobs.data.model.ID
import retrofit2.http.GET
import retrofit2.http.PUT

internal interface ChatService {
  @GET("/chats")
  fun load(token: String): List<ChatData>

  @PUT("/chats")
  fun send(contactId: ID, message: String)


  class ChatData(
      @Json(name = "id") override val id: ID,
      @Json(name = "contactId") override val contactId: ID,
      @Json(name = "direction") private val directionStr: String,
      @Json(name = "text") override val text: String,
      @Json(name = "time") override val time: Long
  ) : Chat {
    override val direction: Chat.Direction get() = Chat.Direction.from(directionStr)
  }
}
