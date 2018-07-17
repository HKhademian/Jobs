package ir.hossainkhademian.jobs.data.api

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.thedeanda.lorem.LoremIpsum
import ir.hossainkhademian.jobs.data.api.model.ChatMock
import ir.hossainkhademian.jobs.data.api.model.JobMock
import ir.hossainkhademian.jobs.data.api.model.LoginMock
import ir.hossainkhademian.jobs.data.api.model.MatchMock
import ir.hossainkhademian.jobs.data.api.model.RequestMock
import ir.hossainkhademian.jobs.data.api.model.SkillMock
import ir.hossainkhademian.jobs.data.api.model.UserMock
import ir.hossainkhademian.jobs.data.api.model.toData
import ir.hossainkhademian.jobs.data.model.*
import java.io.IOException
import java.util.*

internal object MockApiStorage {
  private const val PREF_USERS = "mock.users"
  private const val PREF_JOBS = "mock.jobs"
  private const val PREF_SKILLS = "mock.skills"
  private const val PREF_CHATS = "mock.chats"
  private const val PREF_REQUESTS = "mock.requests"
  private const val PREF_MATCHES = "mock.matches"

  internal lateinit var pref: SharedPreferences
  private val moshi = Moshi.Builder().build()
  private val userAdapter = moshi.adapter(LoginMock::class.java)
  private val jobAdapter = moshi.adapter(JobMock::class.java)
  private val skillAdapter = moshi.adapter(SkillMock::class.java)
  private val chatAdapter = moshi.adapter(ChatMock::class.java)
  private val requestAdapter = moshi.adapter(RequestMock::class.java)
  private val matchAdapter = moshi.adapter(MatchMock::class.java)

  val lorem = LoremIpsum.getInstance()
  val random = Random()

  val users = MockStore<LoginMock>(PREF_USERS, userAdapter, ::initUsers)
  val jobs = MockStore<JobMock>(PREF_JOBS, jobAdapter, ::initJobs)
  val skills = MockStore<SkillMock>(PREF_SKILLS, skillAdapter, ::initSkills)
  val chats = MockStore<ChatMock>(PREF_CHATS, chatAdapter, ::initChats)
  val requests = MockStore<RequestMock>(PREF_REQUESTS, requestAdapter, ::initRequests)
  val matches = MockStore<MatchMock>(PREF_MATCHES, matchAdapter, ::initMatches)

  fun initMockApiStorage(context: Context) {
    pref = context.getSharedPreferences("mock", Context.MODE_PRIVATE)
    load()
  }

  private fun initJobs(store: MockStore<JobMock>) {
    arrayOf("appdev" to "App Developer",
      "androiddec" to "Android App Developer",
      "iosdev" to "iOS App Developer",
      "fullstackdev" to "FullStack Developer",
      "crossdev" to "CrossPlatform App Developer",
      "gamedesigner" to "Game Designer",
      "gamedev" to "Game Developer",
      "robotDev" to "Robot Developer",
      "phpDev" to "Php Server Developer",
      "perstaDev" to "Perstashop Plugin Developer",
      "androidGameDesigner" to "Android Game Designer",
      "cleaner" to "Cleaning Services",
      "guard" to "Guard",
      "clerk" to "Office Clerk",
      "typeman" to "Type guy"
    ).forEach {
      store.items += JobMock(
        id = it.first,
        title = it.second
      )
    }
  }

  private fun initSkills(store: MockStore<SkillMock>) {
    arrayOf("Android", "Kotlin", "Database",
      "iOS", "Dart", "Flutter", "React",
      "ReactNative", "NodeJS", "Server",
      "Mobile", "Desktop", "Python", "SQL",
      "Database", "Test", "UI", "UX", "Java Script",
      "JQuery", "CSS", "LESS", "SASS", "Coffee",
      "PHP", "Scala", "Ruby", "C++", "C", "Delphi",
      "Arduino", "IoT", "ARM", "Atmel", "Code Vision",
      "Debug", "Support", "Robotics", "AI", "Machine Learning",
      "C#", "J#", ".Net", "Visual Basic", "VB.Net", "TypeScript",
      "PhotoShop", "Office", "Excel", "Access", "Word", "PowerPoint",
      "Skype", "OutLook", "Publisher", "Paint").forEach {
      store.items += SkillMock(
        id = it.toLowerCase(),
        title = it
      )
    }
  }

  private fun initUsers(store: MockStore<LoginMock>) {
    (10 until 20).forEach {
      val index = "$it"
      val id = "admin$index"
      store.items += LoginMock(
        id = id,
        title = "Admin $index",
        phone = "+9800000000$index",
        roleStr = UserRole.Admin.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID",
        lastSeen = fakeTime(10)
      )
    }
    (10 until 30).forEach {
      val index = "$it"
      val id = "broker$index"
      store.items += LoginMock(
        id = id,
        title = "Broker $index",
        phone = "+9811111111$index",
        roleStr = UserRole.Broker.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID",
        lastSeen = fakeTime(10)
      )
    }

    (10 until 50).forEach {
      val index = "$it"
      val id = "user$index"
      store.items += LoginMock(
        id = id,
        title = "User $index",
        phone = "+9822222222$index",
        roleStr = UserRole.User.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID",
        lastSeen = fakeTime(10)
      )
    }
  }

  private fun initChats(store: MockStore<ChatMock>) {
  }

  private fun initRequests(store: MockStore<RequestMock>) {
  }

  private fun initMatches(store: MockStore<MatchMock>) {
  }

  private fun load() {
    users.load()
    jobs.load()
    skills.load()
    chats.load()
    requests.load()
    matches.load()
  }

  fun getUserByAccessToken(accessToken: String) =
    users.items.firstOrNull { it.accessToken == accessToken }

  fun fakeWait(errorChance: Int = 5) {
    val delay = 0L + random.nextInt(500) + random.nextInt(1500)
    Thread.sleep(delay)

    if (random.nextInt(100) < errorChance)
      throw IOException("fake error")
  }

  fun rand(max: Int) =
    if (max <= 0) 0 else random.nextInt(max)

  fun fakeDuration(days: Int = 7, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milis: Int = 0) =
    rand(days) * 1000 * 60 * 60 * 24 +
      rand(hours) * 1000 * 60 * 60 +
      rand(minutes) * 1000 * 60 +
      rand(seconds) * 1000 +
      rand(milis)

  fun fakeTime(days: Int = 7, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milis: Int = 0) =
    System.currentTimeMillis() - fakeDuration(days, hours, minutes, seconds, milis)
}
