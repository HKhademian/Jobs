package ir.hossainkhademian.jobs.data.api.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.model.*


class RequestMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "userId") override val userId: ID = emptyID,
  @Json(name = "type") val typeStr: String = "",
  @Json(name = "detail") override val detail: String = "",
  @Json(name = "time") override val time: Long = System.currentTimeMillis(),
  @Json(name = "jobId") override val jobId: ID = emptyID,
  @Json(name = "skillIds") override val skillIds: List<ID> = emptyList(),
  @Json(name = "brokerIds") override val brokerIds: List<ID> = emptyList()
) : Request {
  override val type: RequestType get() = RequestType.from(typeStr)
}

fun RequestMock.copy(
  id: ID? = null,
  userId: ID? = null,
  typeStr: String? = null,
  detail: String? = null,
  time: Long? = null,
  jobId: ID? = null,
  skillIds: List<ID>? = null,
  brokerIds: List<ID>? = null
) = RequestMock(
  id = id ?: this.id,
  userId = userId ?: this.userId,
  typeStr = typeStr ?: this.typeStr,
  detail = detail ?: this.detail,
  time = time ?: this.time,
  jobId = jobId ?: this.jobId,
  skillIds = skillIds ?: this.skillIds,
  brokerIds = brokerIds ?: this.brokerIds
)

fun RequestMock.toData() = RequestData(
  id = id,
  userId = userId,
  typeStr = type.key,
  detail = detail,
  time = time,
  jobId = jobId,
  skillIds = skillIds,
  brokerIds = brokerIds
)

fun Request.toMock() = RequestMock(
  id = id,
  userId = userId,
  typeStr = type.key,
  detail = detail,
  time = time,
  jobId = jobId,
  skillIds = skillIds,
  brokerIds = brokerIds
)

fun Iterable<RequestMock>.toData() = map { it.toData() }
fun <T : Request> Iterable<T>.toMock() = map { it.toMock() }
