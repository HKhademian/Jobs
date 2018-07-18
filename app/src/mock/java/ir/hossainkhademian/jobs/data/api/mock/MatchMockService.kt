@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.api.model.toData
import ir.hossainkhademian.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object MatchMockService : MatchService {
  override fun list(accessToken: String): Call<List<MatchData>> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("no user with this id is found"))

    val matches = when (login.role) {
      UserRole.Admin -> MockApiStorage.matches.items

      UserRole.Broker -> MockApiStorage.matches.items.filterByBrokerId(login.id)

      UserRole.User -> MockApiStorage.matches.items.filterById(
        MockApiStorage.requests.items.filterByUserId(login.id).mapId()
      )
    }


    return Calls.response(matches.toData())
  }

  override fun addMatch(accessToken: String, workerRequestId: ID, companyRequestId: ID, note: String): Call<Unit> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("no user with this id is found"))

    return Calls.failure(IOException("cannot create match from app"))
  }
}
