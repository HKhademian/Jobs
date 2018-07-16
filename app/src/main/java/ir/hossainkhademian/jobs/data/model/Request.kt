package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.DataManager

val EmptyRequest: Request = RequestData(id = emptyID, time = 0L)


interface Request : IdModel {
  override val id: ID
  val userId: ID
  val type: RequestType
  val detail: String
  val time: Long

  val jobId: ID
  val skillIds: List<ID>
  val brokerIds: List<ID>
}

enum class RequestType(val key: String) {
  WORKER("worker"), COMPANY("company");

  companion object {
    fun from(direction: String) = when (direction.toLowerCase()) {
      COMPANY.key.toLowerCase() -> COMPANY
      WORKER.key.toLowerCase() -> WORKER
      else -> WORKER // throw RuntimeException("bad value")
    }
  }
}

val Request.user get() = DataManager.users.findById(userId) ?: EmptyUser
val Request.job get() = DataManager.jobs.findById(jobId) ?: EmptyJob
val Request.skills get() = DataManager.skills.filterById(skillIds)
val Request.brokers get() = DataManager.users.filterById(brokerIds)
val Request.matches get() = DataManager.matches.filterByRequest(type, id)

val RequestType.isWorker get() = this == RequestType.WORKER
val RequestType.isCompany get() = this == RequestType.COMPANY
val Request.isWorker get() = type.isWorker
val Request.isCompany get() = type.isCompany

val Request.title
  get() = when (type) {
//  RequestType.WORKER-> "`${user.title}` seeks Job for `${job.title}`"
//  RequestType.COMPANY-> "`${user.title}` seeks Worker for `${job.title}`"
    RequestType.WORKER -> "`${job.title}` is here from `${user.title}`"
    RequestType.COMPANY -> "`${job.title}` is need from `${user.title}`"
  }
val Request.subtitle
  get() = when (type) {
    RequestType.WORKER -> "has ${skills.joinToString(" , ") { "`${it.title}`" }} skills"
    RequestType.COMPANY -> "needs ${skills.joinToString(" , ") { "`${it.title}`" }} skills"
  }
val Request.avatarUrl get() = job.avatarUrl

class RequestData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "requestId") override val userId: ID = emptyID,
  @Json(name = "type") val typeStr: String = "",
  @Json(name = "detail") override val detail: String = "",
  @Json(name = "time") override val time: Long = System.currentTimeMillis(),
  @Json(name = "jobId") override val jobId: ID = emptyID,
  @Json(name = "skillIds") override val skillIds: List<ID> = emptyList(),
  @Json(name = "brokerIds") override val brokerIds: List<ID> = emptyList()
) : Request {
  override val type: RequestType get() = RequestType.from(typeStr)
}

fun Request.toData() = when (this) {
  is RequestData -> this
  else -> RequestData(
    id = id,
    userId = userId,
    typeStr = type.key,
    detail = detail,
    time = time,
    jobId = jobId,
    skillIds = skillIds,
    brokerIds = brokerIds
  )
}

fun <T : Request> Iterable<T>.toData() = map { it.toData() }

fun <T : Request> Iterable<T>.filterByUserId(userId: ID) =
  filter { it.userId == userId }

fun <T : Request> Iterable<T>.filterByType(type: RequestType) =
  filter { it.type == type }

fun <T : Request> Iterable<T>.filterByJobId(jobId: ID) =
  filter { it.jobId == jobId }

fun <T : Request> Iterable<T>.filterBySkillId(skillId: ID) =
  filter { it.skillIds.contains(skillId) }

fun <T : Request> Iterable<T>.filterBySkillIds(skillIds: Collection<ID>) =
  filter { it.skillIds.containsAll(skillIds) }


fun <T : Request> Iterable<T>.filterByBrokerId(brokerId: ID) =
  filter { it.brokerIds.contains(brokerId) }

fun <T : Request> Iterable<T>.filterByBrokerIds(brokerIds: Collection<ID>) =
  filter { it.brokerIds.containsAll(brokerIds) }

