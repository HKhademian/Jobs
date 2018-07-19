@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.SkillData
import retrofit2.Call
import retrofit2.http.*

interface SkillService {
  @GET("/skills")
  fun list(): Call<List<SkillData>>

  @FormUrlEncoded
  @PUT("/skills")
  fun addSkills(@Header("accessToken") accessToken: String,
                @Field("title") title: String,
                @Field("des") des: String): Call<Unit>
}
