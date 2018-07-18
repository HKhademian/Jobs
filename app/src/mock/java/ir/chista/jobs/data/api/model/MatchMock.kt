package ir.chista.jobs.data.api.model

import com.squareup.moshi.Json
import ir.chista.jobs.data.model.*

class MatchMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "time") override val time: Long = System.currentTimeMillis(),

  @Json(name = "brokerId") override val brokerId: ID = emptyID,
  @Json(name = "brokerState") override val brokerState: MatchState = MatchState.Waiting,
  @Json(name = "brokerNote") override val brokerNote: String = "",

  @Json(name = "workerRequestId") override val workerRequestId: ID = emptyID,
  @Json(name = "workerState") override val workerState: MatchState = MatchState.Waiting,
  @Json(name = "workerNote") override val workerNote: String = "",

  @Json(name = "companyRequestId") override val companyRequestId: ID = emptyID,
  @Json(name = "companyState") override val companyState: MatchState = MatchState.Waiting,
  @Json(name = "companyNote") override val companyNote: String = ""
) : Match

fun MatchMock.copy(
  id: ID? = null,
  time: Long? = null,
  brokerId: ID? = null,
  brokerState: MatchState? = null,
  brokerNote: String? = null,
  workerRequestId: ID? = null,
  workerState: MatchState? = null,
  workerNote: String? = null,
  companyRequestId: ID? = null,
  companyState: MatchState? = null,
  companyNote: String? = null
) = MatchMock(
  id = id ?: this.id,
  time = time ?: this.time,
  brokerId = brokerId ?: this.brokerId,
  brokerState = brokerState ?: this.brokerState,
  brokerNote = brokerNote ?: this.brokerNote,
  workerRequestId = workerRequestId ?: this.workerRequestId,
  workerState = workerState ?: this.workerState,
  workerNote = workerNote ?: this.workerNote,
  companyRequestId = companyRequestId ?: this.companyRequestId,
  companyState = companyState ?: this.companyState,
  companyNote = companyNote ?: this.companyNote
)

fun MatchMock.toData() = MatchData(
  id = id,
  time = time,

  brokerId = brokerId,
  brokerState = brokerState,
  brokerNote = brokerNote,

  workerRequestId = workerRequestId,
  workerState = workerState,
  workerNote = workerNote,

  companyRequestId = companyRequestId,
  companyState = companyState,
  companyNote = companyNote
)

fun Match.toMock() = MatchMock(
  id = id,
  time = time,

  brokerId = brokerId,
  brokerState = brokerState,
  brokerNote = brokerNote,

  workerRequestId = workerRequestId,
  workerState = workerState,
  workerNote = workerNote,

  companyRequestId = companyRequestId,
  companyState = companyState,
  companyNote = companyNote
)

fun Iterable<MatchMock>.toData() = map { it.toData() }
fun <T : Match> Iterable<T>.toMock() = map { it.toMock() }

