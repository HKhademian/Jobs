@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.SkillData
import ir.hossainkhademian.jobs.data.model.toData
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object SkillMock : SkillService {
  override fun list(): Call<List<SkillData>> {
    MockApiStorage.fakeWait()

    return Calls.response(MockApiStorage.skills.map { it.toData() })
  }

  override fun addSkills(accessToken: String, title: String, des: String): Call<Unit> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("no user with this id is found"))

    return Calls.failure(IOException("cannot create skill from app"))
  }
}
