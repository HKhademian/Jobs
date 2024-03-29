@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.AccountManager.isPhoneValid
import ir.chista.jobs.data.api.model.*
import ir.chista.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException
import java.util.*


object AccountMockService : AccountService {
  override fun login(phone: String, password: String): Call<LoginData> {
    MockApiStorage.fakeWait()

    if (phone.isEmpty() || !isPhoneValid(phone))
      return Calls.failure(IOException("Phone format is not currect!"))

    val last = phone.takeLast(1)
    if (last != password)
      return Calls.failure(IOException("Phone & Password is nor matched"))

    val savedUser = MockApiStorage.users.items.firstOrNull { it.phone == phone }
      ?: return Calls.failure(IOException("No user with this phone founds!"))

    val user = savedUser.copy(
      refreshToken = "${savedUser.id}.$generateID",
      accessToken = "${savedUser.id}.$generateID",
      lastSeen = System.currentTimeMillis()
    )
    MockApiStorage.users.update(user)

    return Calls.response(user.toData())
  }

  override fun register(phone: String, title: String): Call<LoginData> {
    MockApiStorage.fakeWait()

    if (phone.isEmpty() || !isPhoneValid(phone))
      return Calls.failure(IOException("Phone format is not currect!"))

    if (phone.isEmpty() || !isPhoneValid(phone))
      return Calls.failure(IOException("Phone format is not currect!"))

    val user = MockApiStorage.users.items.find { it.phone == phone }

    if (user != null)
      return Calls.failure(IOException("User with this phone founds!"))

    val userId = generateID
    val loginData = LoginMock(
      id = userId,
      title = title ?: "User $phone",
      phone = phone,
      roleStr = UserRole.User.key,
      accessToken = "$userId.$generateID",
      refreshToken = "$userId.$generateID"
    )
    MockApiStorage.users.update(loginData)


    /*create fake initial chats */ let {
      MockApiStorage.users.items
        .filter { it.isAdmin /*|| it.isBroker*/ }
        .union(MockApiStorage.users.items
          .filter { it.isUser }
          .shuffled()
          .take(2))
        .flatMap {
          listOf(
            ChatMock(
              senderId = it.id,
              receiverId = userId,
              time = MockApiStorage.fakeTime(),
              message = "Your first messageField from `${it.title}`: wish happy explore :)"
            ),
            ChatMock(
              senderId = it.id,
              receiverId = userId,
              time = MockApiStorage.fakeTime(),
              message = "send me `error` to create a fake api call error"
            ),
            ChatMock(
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
      (1..3).forEach {
        val job = MockApiStorage.jobs.items[MockApiStorage.random.nextInt(MockApiStorage.jobs.items.size)]
        val skills = MockApiStorage.skills.items.shuffled(MockApiStorage.random).take(3)
        val type = if (MockApiStorage.random.nextBoolean()) RequestType.WORKER else RequestType.COMPANY
        val details = when (type) {
          RequestType.WORKER -> "a fake job for a `${job.title}` job, you tell you have ${skills.joinToString(" , ") { "`${it.title}`" }} skills :)"
          RequestType.COMPANY -> "a fake job for a `${job.title}` worker, you tell you need ${skills.joinToString(" , ") { "`${it.title}`" }} skills :)"
        }
        val request = RequestMock(
          userId = userId,
          typeStr = type.key,
          detail = details,
          time = MockApiStorage.fakeTime(),
          jobId = job.id,
          skillIds = skills.map { it.id },
          brokerIds = MockApiStorage.users.items.filterIsBroker().shuffled().take(2 + MockApiStorage.random.nextInt(3)).mapId()
        )
        MockApiStorage.requests.update(request)
      }
    }

    /* write fake sms to user inbox */ let {
      MockApiStorage.notify(
        title = "Your password",
        message = "Thanks for your registration\nphone: $phone\npass: ${phone.last()}\n\njobs.ir"
      )
    }

    return Calls.response(loginData.toData())
  }

  override fun refresh(refreshToken: String): Call<LoginData> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.users.items.find { it.refreshToken == refreshToken }
      ?: return Calls.failure(IOException("cannot find user with this token"))

    val user = login.copy(
      accessToken = "${login.id}.$generateID",
      lastSeen = System.currentTimeMillis()
    )
    MockApiStorage.users.update(user)

    return Calls.response(user.toData())
  }

  override fun changeTitle(refreshToken: String, title: String): Call<LoginData> {
    MockApiStorage.fakeWait()

    val ttl = title.trim()
    if (ttl.length < 4)
      return Calls.failure(IOException("Please use longer title"))
    if (ttl.length > 20)
      return Calls.failure(IOException("Please use smaller title"))

    val login = MockApiStorage.users.items.find { it.refreshToken == refreshToken }
      ?: return Calls.failure(IOException("cannot find user with this token"))

    val user = login.copy(
      title = ttl,
      lastSeen = System.currentTimeMillis()
    )
    MockApiStorage.users.update(user)

    return Calls.response(user.toData())
  }
}
