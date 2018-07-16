package ir.hossainkhademian.jobs.data.api

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.thedeanda.lorem.LoremIpsum
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
  private val userAdapter = moshi.adapter(LoginData::class.java)
  private val jobAdapter = moshi.adapter(JobData::class.java)
  private val skillAdapter = moshi.adapter(SkillData::class.java)
  private val chatAdapter = moshi.adapter(ChatData::class.java)
  private val requestAdapter = moshi.adapter(RequestData::class.java)
  private val matchAdapter = moshi.adapter(MatchData::class.java)

  val lorem = LoremIpsum.getInstance()
  val random = Random()

  val users = MockStore<Login, LoginData>(PREF_USERS, userAdapter, Login::toData, ::initUsers)
  val jobs = MockStore<Job, JobData>(PREF_JOBS, jobAdapter, Job::toData, ::initJobs)
  val skills = MockStore<Skill, SkillData>(PREF_SKILLS, skillAdapter, Skill::toData, ::initSkills)
  val chats = MockStore<Chat, ChatData>(PREF_CHATS, chatAdapter, Chat::toData, ::initChats)
  val requests = MockStore<Request, RequestData>(PREF_REQUESTS, requestAdapter, Request::toData, ::initRequests)
  val matches = MockStore<Match, MatchData>(PREF_MATCHES, matchAdapter, Match::toData, ::initMatches)

  fun initMockApiStorage(context: Context) {
    pref = context.getSharedPreferences("mock", Context.MODE_PRIVATE)
    load()
  }

  private fun initJobs(store: MockStore<Job, JobData>) {
    arrayOf("appdev" to "App Developer",
      "androiddec" to "Android App Developer",
      "iosdev" to "iOS App Developer",
      "fullstackdev" to "FullStack Developer",
      "crossdev" to "CrossPlatform App Developer",
      "gamedesigner" to "Game Designer",
      "gamedev" to "Game Developer").forEach {
      store.items += JobData(
        id = it.first,
        title = it.second
      )
    }
  }

  private fun initSkills(store: MockStore<Skill, SkillData>) {
    arrayOf("Android", "Kotlin", "Database",
      "iOS", "Dart", "Flutter", "React",
      "ReactNative", "NodeJS", "Server",
      "Mobile", "Desktop").forEach {
      store.items += SkillData(
        id = it.toLowerCase(),
        title = it
      )
    }
  }

  private fun initUsers(store: MockStore<Login, LoginData>) {
    (1 until 3).forEach {
      val index = "0$it"
      val id = "admin$index"
      store.items += LoginData(
        id = id,
        title = "Admin $index",
        phone = "+9800000000$index",
        roleStr = UserRole.Admin.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID"
      )
    }
    (1 until 5).forEach {
      val index = "0$it"
      val id = "broker$index"
      store.items += LoginData(
        id = id,
        title = "Broker $index",
        phone = "+9811111111$index",
        roleStr = UserRole.Broker.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID"
      )
    }

    (10 until 25).forEach {
      val index = "0$it"
      val id = "user$index"
      store.items += LoginData(
        id = id,
        title = "User $index",
        phone = "+982222222$index",
        roleStr = UserRole.User.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID"
      )
    }
  }

  private fun initChats(store: MockStore<Chat, ChatData>) {
  }

  private fun initRequests(store: MockStore<Request, RequestData>) {
  }

  private fun initMatches(store: MockStore<Match, MatchData>) {
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
}
