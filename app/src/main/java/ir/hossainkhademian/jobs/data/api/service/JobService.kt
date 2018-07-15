@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.JobData
import retrofit2.Call
import retrofit2.http.GET

interface JobService {
  @GET("/jobs")
  fun list(): Call<List<JobData>>
}
