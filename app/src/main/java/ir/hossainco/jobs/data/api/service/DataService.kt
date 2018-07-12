package ir.hossainco.jobs.data.api

import com.squareup.moshi.Json
import ir.hossainco.jobs.data.model.Job
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

internal interface DataService {
  @GET("/jobs")
  fun getJobs(): List<JobData>


  @GET("/skills")
  fun getSkills(): List<SkillData>

  @PUT("/skills")
  fun addSkills(title: String, des: String)


  @PUT("/chats")
  fun addSkills(title: String, des: String)



  class JobData(
      @Json(name = "id") override val id: String,
      @Json(name = "title") override val title: String
  ) : Job

  class SkillData(
      @Json(name = "id") override val id: String,
      @Json(name = "title") override val title: String
  ) : Job
}
