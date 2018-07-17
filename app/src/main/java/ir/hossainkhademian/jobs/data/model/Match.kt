package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.data.DataManager

val EmptyMatch: Match = MatchData(id = emptyID)

interface Match : IdModel {
  override val id: ID
  val time: Long

  val brokerId: ID
  val brokerState: MatchState
  val brokerNote: String

  val workerRequestId: ID
  val workerState: MatchState
  val workerNote: String

  val companyRequestId: ID
  val companyState: MatchState
  val companyNote: String
}

interface LocalMatch : Match {
}

enum class MatchState(val key: String) {
  Waiting("waiting"),
  Accepted("accepted"),
  Rejected("rejected");

  companion object {
    fun from(state: String) = when (state) {
      Accepted.key.toLowerCase() -> Accepted
      Rejected.key.toLowerCase() -> Rejected
      else -> Waiting
    }
  }
}

val LocalMatch.broker get() = DataManager.users.findById(brokerId)
val LocalMatch.workerRequest get() = DataManager.requests.findById(workerRequestId)
val LocalMatch.companyRequest get() = DataManager.requests.findById(companyRequestId)

val Match.state
  get() = when {
    isRejected -> MatchState.Rejected
    isAccepted -> MatchState.Accepted
    else -> MatchState.Waiting
  }

fun LocalMatch.getNote(userId: ID) = when (userId) {
  brokerId -> brokerNote
  workerRequest?.userId -> workerNote
  companyRequest?.userId -> companyNote
  else -> ""
}

val LocalMatch.note get() = getNote(AccountManager.id)
val Match.isRejected get() = brokerState.isRejected || workerState.isRejected || companyState.isRejected
val Match.isAccepted get() = brokerState.isAccepted && workerState.isAccepted && companyState.isAccepted
val MatchState.isRejected get() = this == MatchState.Rejected
val MatchState.isAccepted get() = this == MatchState.Accepted

class MatchData(
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
) : LocalMatch

fun Match.toData() = when (this) {
  is MatchData -> this
  else -> MatchData(
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
}

fun <T : Match> Iterable<T>.toData() = map { it.toData() }

fun <T : Match> Iterable<T>.filterByRequest(requestType: RequestType, requestId: ID) = when (requestType) {
  RequestType.WORKER -> filterByWorkerRequestId(requestId)
  RequestType.COMPANY -> filterByCompanyRequestId(requestId)
}

fun <T : Match> Iterable<T>.filterByWorkerRequestId(requestId: ID) =
  filter { it.workerRequestId == requestId }

fun <T : Match> Iterable<T>.filterByCompanyRequestId(requestId: ID) =
  filter { it.companyRequestId == requestId }

fun <T : Match> Iterable<T>.filterByBrokerId(userId: ID) =
  filter { it.brokerId == userId }

fun <T : LocalMatch> Iterable<T>.filterByWorkerId(userId: ID) =
  filter { it.workerRequest?.userId == userId }

fun <T : LocalMatch> Iterable<T>.filterByCompanyId(userId: ID) =
  filter { it.companyRequest?.userId == userId }

fun <T : LocalMatch> Iterable<T>.filterByUserId(userId: ID) =
  filter { it.brokerId == userId || it.workerRequest?.userId == userId || it.companyRequest?.userId == userId }
