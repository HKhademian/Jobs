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

    return Calls.response(MockApiStorage.requests.items.filterByUserId(login.id).toData())
  }

  override fun edit(accessToken: String, id: ID, typeStr: String, detail: String, jobId: ID, skillIds: List<ID>): Call<RequestData> {
    MockApiStorage.fakeWait()

    return Calls.failure(IOException("not implemented yet!"))
  }

  override fun create(accessToken: String, id: ID, typeStr: String, detail: String, jobId: ID, skillIds: List<ID>): Call<RequestData> {
    MockApiStorage.fakeWait()

    return Calls.failure(IOException("not implemented yet!"))
  }
}
