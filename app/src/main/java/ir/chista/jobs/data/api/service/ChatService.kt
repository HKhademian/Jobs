@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ChatData
import ir.chista.jobs.data.model.ID
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ChatService {
  @GET("/chats")
  fun list(accessToken: String): Call<List<ChatData>>

  @GET("/chats")
  fun list(accessToken: String, contactId: ID): Call<List<ChatData>>

  @PUT("/chats")
  fun send(accessToken: String, contactId: ID, message: String): Call<List<ChatData>> // Call<ChatData>

  @POST("/chats/seen")
  fun markAsSeen(accessToken: String, senderId: ID): Call<List<ChatData>>
}
