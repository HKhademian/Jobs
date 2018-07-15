package ir.hossainkhademian.jobs.data.api

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.thedeanda.lorem.LoremIpsum
import ir.hossainkhademian.jobs.data.model.*
import java.io.IOException
import java.util.*

internal object MockApiStorage {
  private const val PREF_JOBS = "mock.jobsList"
  private const val PREF_SKILLS = "mock.skillsList"
  private const val PREF_USERS = "mock.usersList"
  private const val PREF_CHATS = "mock.userChats"
  private const val PREF_REQUESTS = "mock.requestsList"

  private lateinit var pref: SharedPreferences
  private val moshi = Moshi.Builder().build()
  private val jobAdapter = moshi.adapter(JobData::class.java)
  private val skillAdapter = moshi.adapter(SkillData::class.java)
  private val userAdapter = moshi.adapter(LoginData::class.java)
  private val chatAdapter = moshi.adapter(ChatData::class.java)
  private val requestAdapter = moshi.adapter(RequestData::class.java)

  val lorem = LoremIpsum.getInstance()
  val random = Random()

  val users = arrayListOf<Login>()
  val savedUsers = arrayListOf<Login>()

  val jobs = arrayListOf<Job>()
  val savedJobs = arrayListOf<Job>()

  val skills = arrayListOf<Skill>()
  val savedSkills = arrayListOf<Skill>()

  val chats = arrayListOf<Chat>()
  val savedChats = arrayListOf<Chat>()

  val requests = arrayListOf<Request>()
  val savedRequests = arrayListOf<Request>()

  fun initMockApiStorage(context: Context) {
    pref = context.getSharedPreferences("mock", Context.MODE_PRIVATE)
    initJobs()
    initSkills()
    initUsers()
    initChats()
    initRequests()
    load()
  }

  private fun initJobs() {
    arrayOf("appdev" to "App Developer",
      "androiddec" to "Android App Developer",
      "iosdev" to "iOS App Developer",
      "fullstackdev" to "FullStack Developer",
      "crossdev" to "CrossPlatform App Developer",
      "gamedesigner" to "Game Designer",
      "gamedev" to "Game Developer").forEach {
      jobs += JobData(
        id = it.first,
        title = it.second
      )
    }
  }

  private fun initSkills() {
    arrayOf("Android", "Kotlin", "Database",
      "iOS", "Dart", "Flutter", "React",
      "ReactNative", "NodeJS", "Server",
      "Mobile", "Desktop").forEach {
      skills += SkillData(
        id = it.toLowerCase(),
        title = it
      )
    }
  }

  private fun initUsers() {
    (1 until 3).forEach {
      val index = "0$it"
      val id = "admin$index"
      users += LoginData(
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
      users += LoginData(
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
      users += LoginData(
        id = id,
        title = "User $index",
        phone = "+982222222$index",
        roleStr = UserRole.User.key,
        accessToken = "$id.$generateID",
        refreshToken = "$id.$generateID"
      )
    }
  }

  private fun initChats() {
  }

  private fun initRequests() {
  }

  private fun load() {
    savedJobs.clear()
    pref
      .getStringSet(PREF_JOBS, emptySet())
      .map { jobAdapter.fromJson(it)!! }
      .forEach { update(it, false) }

    savedSkills.clear()
    pref
      .getStringSet(PREF_SKILLS, emptySet())
      .map { skillAdapter.fromJson(it)!! }
      .forEach { update(it, false) }

    savedUsers.clear()
    pref
      .getStringSet(PREF_USERS, emptySet())
      .map { userAdapter.fromJson(it)!! }
      .forEach { update(it, false) }

    savedChats.clear()
    pref
      .getStringSet(PREF_CHATS, emptySet())
      .map { chatAdapter.fromJson(it)!! }
      .forEach { update(it, false) }

    savedRequests.clear()
    pref
      .getStringSet(PREF_REQUESTS, emptySet())
      .map { requestAdapter.fromJson(it)!! }
      .forEach { update(it, false) }
  }

  fun update(item: Job, save: Boolean = true) {
    savedJobs.removeAll { it.id == item.id }
    jobs.removeAll { it.id == item.id }

    savedJobs += item
    jobs += item

    if (save)
      pref.edit().putStringSet(PREF_JOBS,
        savedJobs.map { it.toData() }.map { jobAdapter.toJson(it)!! }.toSet()
      ).apply()
  }

  fun update(item: Skill, save: Boolean = true) {
    savedSkills.removeAll { it.id == item.id }
    skills.removeAll { it.id == item.id }

    savedSkills += item
    skills += item

    if (save)
      pref.edit().putStringSet(PREF_SKILLS,
        savedSkills.map { it.toData() }.map { skillAdapter.toJson(it)!! }.toSet()
      ).apply()
  }

  fun update(item: Login, save: Boolean = true) {
    savedUsers.removeAll { it.id == item.id }
    users.removeAll { it.id == item.id }

    savedUsers += item
    users += item

    if (save)
      pref.edit().putStringSet(PREF_USERS,
        savedUsers.map { it.toData() }.map { userAdapter.toJson(it)!! }.toSet()
      ).apply()
  }

  fun update(item: Chat, save: Boolean = true) {
    savedChats.removeAll { it.id == item.id }
    chats.removeAll { it.id == item.id }

    savedChats += item
    chats += item

    if (save)
      pref.edit().putStringSet(PREF_CHATS,
        savedChats.map { it.toData() }.map { chatAdapter.toJson(it)!! }.toSet()
      ).apply()
  }

  fun update(item: Request, save: Boolean = true) {
    savedRequests.removeAll { it.id == item.id }
    requests.removeAll { it.id == item.id }

    savedRequests += item
    requests += item

    if (save)
      pref.edit().putStringSet(PREF_REQUESTS,
        savedRequests.map { it.toData() }.map { requestAdapter.toJson(it)!! }.toSet()
      ).apply()
  }

  fun getUserByAccessToken(accessToken: String) =
    users.firstOrNull { it.accessToken == accessToken }

  fun fakeWait(errorChance: Int = 5) {
    val delay = 0L + random.nextInt(500) + random.nextInt(1500)
    Thread.sleep(delay)

    if (random.nextInt(100) < errorChance)
      throw IOException("fake error")
  }
}
