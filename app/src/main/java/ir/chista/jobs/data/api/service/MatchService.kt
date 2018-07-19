@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.MatchData
import retrofit2.Call
import retrofit2.http.*

interface MatchService {
  @GET("/matches")
  fun list(@Header("accessToken") accessToken: String): Call<List<MatchData>>

  @FormUrlEncoded
  @PUT("/matches")
  fun addMatch(@Header("accessToken") accessToken: String,
               @Field("workerRequestId")workerRequestId: ID,
               @Field("companyRequestId") companyRequestId: ID,
               @Field("note") note: String): Call<Unit>
}
