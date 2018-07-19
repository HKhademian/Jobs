@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.Request
import ir.chista.jobs.data.model.RequestData
import retrofit2.Call
import retrofit2.http.*

interface RequestService {
  @GET("/requests")
  fun list(@Header("accessToken") accessToken: String): Call<List<RequestData>>

  @FormUrlEncoded
  @POST("/requests")
  fun edit(@Header("accessToken") accessToken: String,
           @Field("String") requestId: ID,
           @Field("typeStr") typeStr: String,
           @Field("jobId") jobId: ID,
           @Field("skillIds") skillIds: List<ID>,
           @Field("detail") detail: String): Call<RequestData>

  @FormUrlEncoded
  @PUT("/requests")
  fun create(@Header("accessToken") accessToken: String,
             @Field("typeStr")typeStr: String,
             @Field("jobId") jobId: ID,
             @Field("skillIds") skillIds: List<ID>,
             @Field("detail") detail: String): Call<RequestData>

  @FormUrlEncoded
  @PUT("/requests/broker")
  fun addBroker(@Header("accessToken") accessToken: String,
                @Field("requestId") requestId: ID,
                @Field("brokerId") brokerId: ID): Call<RequestData>

  @FormUrlEncoded
  @DELETE("/requests/broker")
  fun removeBroker(@Header("accessToken") accessToken: String,
                   @Field("requestId")requestId: ID,
                   @Field("brokerId") brokerId: ID): Call<RequestData>
}
