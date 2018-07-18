@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.api.model.RequestMock
import ir.chista.jobs.data.api.model.copy
import ir.chista.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

object RequestMockService : RequestService {
  override fun list(accessToken: String): Call<List<RequestData>> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val requests = when (login.role) {
      UserRole.Admin -> MockApiStorage.requests.items

      UserRole.Broker -> MockApiStorage.requests.items.filterByBrokerId(login.id)

      UserRole.User -> MockApiStorage.requests.items.filterByUserId(login.id)
    }

    return Calls.response(requests.toData())
  }

  override fun edit(accessToken: String, requestId: ID, typeStr: String, jobId: ID, skillIds: List<ID>, detail: String): Call<RequestData> {
    MockApiStorage.fakeWait()

    if (skillIds.isEmpty())
      return Calls.failure(IOException("please select some skills"))
    if (skillIds.count() > 5)
      return Calls.failure(IOException("please select less skills"))

    if (detail.isEmpty())
      return Calls.failure(IOException("please specify some details"))

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val request = MockApiStorage.requests.items.findById(requestId)
      ?: return Calls.failure(IOException("no Request with this requestId found"))

    MockApiStorage.jobs.items.findById(jobId)
      ?: return Calls.failure(IOException("no Job with this jobId found"))

    skillIds.forEach {
      MockApiStorage.skills.items.findById(it)
        ?: return Calls.failure(IOException("no Skill with this skillId in skillIds found"))
    }

    if (request.userId != login.id) { // request owner
      if (login.isNotAdmin) { // all admins
        MockApiStorage.users.items.filterById(request.brokerIds).findById(login.id) ?: // all requests brokers
        return Calls.failure(IOException("this login has not required permission to this action"))
      }
    }

    val newRequest = request.copy(
      jobId = if (login.isAdmin) jobId else request.id,
      skillIds = skillIds,
      detail = detail
    )
    MockApiStorage.requests.update(newRequest)

    return Calls.response(newRequest.toData())
  }

  override fun create(accessToken: String, typeStr: String, jobId: ID, skillIds: List<ID>, detail: String): Call<RequestData> {
    MockApiStorage.fakeWait()

    val type = RequestType.from(typeStr)
      ?: return Calls.failure(IOException("please select a valid type"))

    if (skillIds.isEmpty())
      return Calls.failure(IOException("please select some skills"))
    if (skillIds.count() > 5)
      return Calls.failure(IOException("please select less skills"))

    if (detail.isEmpty())
      return Calls.failure(IOException("please specify some details"))

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    MockApiStorage.jobs.items.findById(jobId)
      ?: return Calls.failure(IOException("no Job with this jobId found"))

    skillIds.forEach {
      MockApiStorage.skills.items.findById(it)
        ?: return Calls.failure(IOException("no Skill with this skillId in skillIds found"))
    }

    val newRequest = RequestMock(
      userId = login.id,
      typeStr = type.key,
      jobId = jobId,
      skillIds = skillIds,
      detail = detail
    )
    MockApiStorage.requests.update(newRequest)

    return Calls.response(newRequest.toData())
  }

}
