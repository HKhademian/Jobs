@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.UserData
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
  @GET("/users")
  @Deprecated(message = "deprected", replaceWith = ReplaceWith("list"))
  fun get(accessToken: String, id: ID): Call<UserData>

  @GET("/users")
  @Deprecated(message = "deprected", replaceWith = ReplaceWith("list"))
  fun list(accessToken: String, ids: List<ID>): Call<List<UserData>>

  @GET("/users")
  fun list(accessToken: String): Call<List<UserData>>
}
