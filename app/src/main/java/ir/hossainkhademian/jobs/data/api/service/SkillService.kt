@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.SkillData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT

interface SkillService {
  @GET("/skills")
  fun list(): Call<List<SkillData>>

  @PUT("/skills")
  fun addSkills(accessToken: String, title: String, des: String): Call<Unit>
}
