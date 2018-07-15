@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.UserData
import ir.hossainkhademian.jobs.data.model.toData
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object UserMock : UserService {
  override fun get(id: ID): Call<UserData> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.users.firstOrNull { it.id == id }
      ?: return Calls.failure(IOException("no user with this id is found"))

    return Calls.response(user.toData())
  }

  override fun list(ids: List<ID>): Call<List<UserData>> {
    MockApiStorage.fakeWait()

    val users = MockApiStorage.users.filter { ids.contains(it.id) }.map { it.toData() }
    if (users.isEmpty()) return Calls.failure(IOException("no user with these ids is found"))

    return Calls.response(users)
  }
}
