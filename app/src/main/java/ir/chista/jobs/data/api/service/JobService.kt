@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.JobData
import retrofit2.Call
import retrofit2.http.GET

interface JobService {
  @GET("/jobs")
  fun list(): Call<List<JobData>>
}
