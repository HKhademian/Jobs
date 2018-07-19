@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.LoginData
import retrofit2.Call
import retrofit2.http.*

interface AccountService {
  @FormUrlEncoded
  @POST("/account/login")
  fun login(@Field("phone") phone: String,
            @Field("password") password: String): Call<LoginData>

  @FormUrlEncoded
  @POST("/account/register")
  fun register(@Field("phone") phone: String): Call<LoginData>

  @POST("/account/update")
  fun refresh(@Header("refreshToken") refreshToken: String): Call<LoginData>

  @FormUrlEncoded
  @POST("/account/change")
  fun changeTitle(@Header("accessToken") accessToken: String,
                  @Field("title") title: String): Call<LoginData>
}
