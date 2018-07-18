@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object UserMockService : UserService {
  override fun get(accessToken: String, id: ID): Call<UserData> {
    return Calls.failure(IOException("Deprected API"))
    //MockApiStorage.fakeWait()
    //
    //val login = MockApiStorage.getUserByAccessToken(accessToken)
    //  ?: return Calls.failure(IOException("no user with this id is found"))
    //
    //val user = MockApiStorage.users.items.findById(id)
    //  ?: return Calls.failure(IOException("no user with this id is found"))
    //
    //return Calls.response(user.toData())
  }

  override fun list(accessToken: String, ids: List<ID>): Call<List<UserData>> {
    return Calls.failure(IOException("Deprected API"))

    //MockApiStorage.fakeWait()
    //
    //val login = MockApiStorage.getUserByAccessToken(accessToken)
    //  ?: return Calls.failure(IOException("no user with this id is found"))
    //
    //val users = MockApiStorage.users.items.filterById(ids).toData()
    //// if (users.isEmpty()) return Calls.failure(IOException("no user with these ids is found"))
    //
    //return Calls.response(users)
  }

  override fun list(accessToken: String): Call<List<UserData>> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("no user with this id is found"))

    val users = when (login.role) {
      UserRole.Admin -> MockApiStorage.users.items

      UserRole.Broker -> MockApiStorage.users.items.filterById(
        MockApiStorage.chats.items
          .filterByContactId(login.id)
          .flatMap { listOf(it.senderId, it.receiverId) }
          .union(
            MockApiStorage.requests.items.filterByUserId(login.id).flatMap { it.brokerIds }
          )
          .union(
            MockApiStorage.users.items.filterIsAdmin().mapId()
          ).union(
            MockApiStorage.users.items.filterIsBroker().mapId()
          ).union(
            MockApiStorage.requests.items.filterByBrokerId(login.id).map { it.userId }
          )
      )

      UserRole.User -> MockApiStorage.users.items.filterById(
        MockApiStorage.chats.items
          .filterByContactId(login.id)
          .flatMap { listOf(it.senderId, it.receiverId) }
          .union(
            MockApiStorage.requests.items.filterByUserId(login.id).flatMap { it.brokerIds }
          )
          .union(
            MockApiStorage.users.items.filterIsAdmin().mapId()
          )
      )
    }

    return Calls.response(users.toData())
  }
}
