@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.MatchData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT

interface MatchService {
  @GET("/matches")
  fun list(accessToken: String): Call<List<MatchData>>

  @PUT("/matches")
  fun addMatch(accessToken: String, workerRequestId: ID, companyRequestId: ID, note: String): Call<Unit>
}
