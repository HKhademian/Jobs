@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.UserData
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
  @GET("/users")
  fun get(id: ID): Call<UserData>

  @GET("/users")
  fun list(ids: List<ID>): Call<List<UserData>>
}
