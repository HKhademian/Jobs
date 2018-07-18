@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.LoginData
import retrofit2.Call
import retrofit2.http.POST

interface AccountService {
  @POST("/account/login")
  fun login(phone: String, password: String): Call<LoginData>

  @POST("/account/register")
  fun register(phone: String): Call<LoginData>

  @POST("/account/update")
  fun refresh(refreshToken: String): Call<LoginData>

  @POST("/account/change")
  fun changeTitle(refreshToken: String, title: String): Call<LoginData>
}
