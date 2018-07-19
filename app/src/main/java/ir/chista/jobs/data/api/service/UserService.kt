@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface UserService {
  @GET("/users")
  fun list(@Header("accessToken")  accessToken: String): Call<List<UserData>>
}
