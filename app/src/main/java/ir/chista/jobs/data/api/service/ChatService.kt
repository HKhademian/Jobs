@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ChatData
import ir.chista.jobs.data.model.ID
import retrofit2.Call
import retrofit2.http.*

interface ChatService {
  @GET("/chats")
  fun list(@Header("accessToken")accessToken: String): Call<List<ChatData>>

  @FormUrlEncoded
  @GET("/chats")
  fun list(@Header("accessToken")accessToken: String,
           @Header("contactId")contactId: ID): Call<List<ChatData>>

  @FormUrlEncoded
  @PUT("/chats")
  fun send(@Header("accessToken") accessToken: String,
           @Field("contactId")  contactId: ID,
           @Field("message") message: String): Call<List<ChatData>> // Call<ChatData>

  @FormUrlEncoded
  @POST("/chats/seen")
  fun markAsSeen(@Header("accessToken") accessToken: String,
                 @Field("senderId") senderId: ID): Call<List<ChatData>>
}
