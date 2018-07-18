@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object RequestMockService : RequestService {
  override fun list(accessToken: String): Call<List<RequestData>> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val requests = when (login.role) {
      UserRole.Admin -> MockApiStorage.requests.items

      UserRole.Broker -> MockApiStorage.requests.items.filterByBrokerId(login.id)

      UserRole.User -> MockApiStorage.requests.items.filterByUserId(login.id)
    }

    return Calls.response(requests.toData())
  }

  override fun edit(accessToken: String, requestId: ID, typeStr: String, jobId: ID, skillIds: Collection<ID>, detail: String): Call<RequestData> {
    MockApiStorage.fakeWait()
    Thread.sleep(10000)

    return Calls.failure(IOException("not implemented yet!"))
  }

  override fun create(accessToken: String, typeStr: String, jobId: ID, skillIds: Collection<ID>, detail: String): Call<RequestData> {
    MockApiStorage.fakeWait()
    Thread.sleep(10000)

    return Calls.failure(IOException("not implemented yet!"))
  }
}
