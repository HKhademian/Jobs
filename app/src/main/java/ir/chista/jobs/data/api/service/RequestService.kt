@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.Request
import ir.chista.jobs.data.model.RequestData
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RequestService {
  @GET("/requests")
  fun list(accessToken: String): Call<List<RequestData>>

  @POST("/requests")
  fun edit(accessToken: String, requestId: ID, typeStr: String, jobId: ID, skillIds: List<ID>, detail: String): Call<RequestData>

  @PUT("/requests")
  fun create(accessToken: String, typeStr: String, jobId: ID, skillIds: List<ID>, detail: String): Call<RequestData>

  @PUT("/requests/broker")
  fun addBroker(accessToken: String, requestId: ID, brokerId: ID): Call<RequestData>

  @DELETE("/requests/broker")
  fun removeBroker(accessToken: String, requestId: ID, brokerId: ID): Call<RequestData>
}
