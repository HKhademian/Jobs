@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.MatchData
import ir.hossainkhademian.jobs.data.api.model.toData
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object MatchMock : MatchService {
  override fun list(accessToken: String): Call<List<MatchData>> {
    MockApiStorage.fakeWait()

    return Calls.response(MockApiStorage.matches.items.toData())
  }

  override fun addMatch(accessToken: String, workerRequestId: ID, companyRequestId: ID, note: String): Call<Unit> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("no user with this id is found"))

    return Calls.failure(IOException("cannot create match from app"))
  }
}
