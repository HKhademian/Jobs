@file:Suppress("PackageDirectoryMismatch")

package ir.hossainco.jobs.data.api

import com.squareup.moshi.Json
import ir.hossainco.jobs.data.model.ID
import ir.hossainco.jobs.data.model.Request
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

internal interface RequestService {
  @GET("/requests")
  fun load(token: String): List<Json>

  @POST("/requests")
  fun edit(id: ID, typeStr: String, detail: String, jobId: ID, skillIds: List<ID>)

  @PUT("/requests")
  fun create(id: ID, typeStr: String, detail: String, jobId: ID, skillIds: List<ID>)


  fun edit(request: Request) =
      edit(request.id, request.type.key, request.detail, request.jobId, request.skillIds)

  fun create(request: Request) =
      create(request.id, request.type.key, request.detail, request.jobId, request.skillIds)


  class RequestData(
      @Json(name = "id") override val id: ID,
      @Json(name = "type") private val typeStr: String,
      @Json(name = "detail") override val detail: String,
      @Json(name = "jobId") override val jobId: ID,
      @Json(name = "skillIds") override val skillIds: List<ID>
  ) : Request {
    override val type: Request.Type get() = Request.Type.from(typeStr)
  }
}
