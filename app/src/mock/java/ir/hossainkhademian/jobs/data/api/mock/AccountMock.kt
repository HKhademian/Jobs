@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.AccountManager.isPhoneValid
import ir.hossainkhademian.jobs.data.model.*
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException
import java.util.*

object AccountMock : AccountService {
  override fun login(phone: String, password: String): Call<LoginData> {
    MockApiStorage.fakeWait()

    if (phone.isEmpty() || !isPhoneValid(phone))
      return Calls.failure(IOException("Phone format is not currect!"))

    val last = phone.takeLast(1)
    if (last != password)
      return Calls.failure(IOException("Phone & Password is nor matched"))

    val user = MockApiStorage.users.items.firstOrNull { it.phone == phone }
      ?: return Calls.failure(IOException("No user with this phone founds!"))

    return Calls.response(user.toData())
  }

  override fun register(phone: String): Call<LoginData> {
    MockApiStorage.fakeWait()

    if (phone.isEmpty() || !isPhoneValid(phone))
      return Calls.failure(IOException("Phone format is not currect!"))

    val last = phone.takeLast(1)
    if (last == "0" || last == "1")
      return Calls.failure(IOException("cannot create admin/broker usersList from App"))

    val user = MockApiStorage.users.items.firstOrNull { it.phone == phone }

    if (user != null)
      return Calls.failure(IOException("User with this phone founds!"))

    val userId = UUID.randomUUID().toString()
    val loginData = LoginData(
      id = userId,
      title = "User $phone",
      phone = phone,
      roleStr = UserRole.User.key,
      accessToken = "$userId.$generateID",
      refreshToken = "$userId.$generateID"
    )
    MockApiStorage.users.update(loginData)


    /*create fake initial chats */ let {
      MockApiStorage.users.items
        .filter { it.isAdmin || it.isBroker }
        .union(MockApiStorage.users.items
          .filter { !it.isAdmin && !it.isBroker }
          .shuffled()
          .take(5))
        .flatMap {
          listOf(
            ChatData(
              senderId = it.id,
              receiverId = userId,
              time = MockApiStorage.fakeTime(),
              message = "Your first messageField from `${it.title}`: wish happy explore :)"
            ),
            ChatData(
              senderId = it.id,
              receiverId = userId,
              time = MockApiStorage.fakeTime(),
              message = "send me `error` to create a fake api call error"
            ),
            ChatData(
              senderId = it.id,
              receiverId = userId,
              time = MockApiStorage.fakeTime(),
              message = "send me `answer` to answer you with fake message"
            )
          )
        }
        .forEach { MockApiStorage.chats.update(it) }
    }

    /*create fake initial requests */ let {
      (1..10).forEach {
        val job = MockApiStorage.jobs.items[MockApiStorage.random.nextInt(MockApiStorage.jobs.items.size)]
        val skills = MockApiStorage.skills.items.shuffled(MockApiStorage.random).take(3)
        val type = if (MockApiStorage.random.nextBoolean()) RequestType.WORKER else RequestType.COMPANY
        val details = when (type) {
          RequestType.WORKER -> "a fake request for a `${job.title}` job, you tell you have ${skills.joinToString(" , ") { "`${it.title}`" }} skills :)"
          RequestType.COMPANY -> "a fake request for a `${job.title}` worker, you tell you need ${skills.joinToString(" , ") { "`${it.title}`" }} skills :)"
        }
        val request = RequestData(
          userId = userId,
          typeStr = type.key,
          detail = details,
          time = MockApiStorage.fakeTime(),
          jobId = job.id,
          skillIds = skills.map { it.id },
          brokerIds = MockApiStorage.users.items.filterIsBroker().shuffled().take(MockApiStorage.random.nextInt(3)).mapId()
        )
        MockApiStorage.requests.update(request)
      }
    }

    return Calls.response(loginData)
  }

  override fun refresh(refreshToken: String): Call<LoginData> {
    Thread.sleep(2000)

    val oldUser = MockApiStorage.users.items.firstOrNull { it.refreshToken == refreshToken }
      ?: return Calls.failure(IOException("cannot update this token"))

    val user = oldUser.copy(
      refreshToken = "${oldUser.id}.$generateID",
      lastSeen = System.currentTimeMillis()
    )
    MockApiStorage.users.update(user)

    return Calls.response(user.toData())
  }
}
