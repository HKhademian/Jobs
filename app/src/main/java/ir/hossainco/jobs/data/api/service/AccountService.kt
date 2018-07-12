@file:Suppress("PackageDirectoryMismatch")

package ir.hossainco.jobs.data.api

import com.squareup.moshi.Json
import retrofit2.http.POST

internal interface AccountService {
  @POST("/account/login")
  fun login(phone: String, password: String): ProfileData

  @POST("/account/register")
  fun register(phone: String, password: String): ProfileData

  @POST("/account/refresh")
  fun refresh(refreshToken: String): ProfileData

  class ProfileData(
      @Json(name = "id") val id: String,
      @Json(name = "title") val title: String,
      @Json(name = "phone") val phone: String,
      @Json(name = "role") val role: String,
      @Json(name = "accessToken") val accessToken: String = "",
      @Json(name = "refreshToken") val refreshToken: String = ""
  )
}
