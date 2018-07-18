@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.api.model.toData
import ir.chista.jobs.data.model.SkillData
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object SkillMockService : SkillService {
  override fun list(): Call<List<SkillData>> {
    MockApiStorage.fakeWait()

    return Calls.response(MockApiStorage.skills.items.toData())
  }

  override fun addSkills(accessToken: String, title: String, des: String): Call<Unit> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("no user with this id is found"))

    return Calls.failure(IOException("cannot create skill from app"))
  }
}
