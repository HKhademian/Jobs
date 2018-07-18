@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "Match")
internal class MatchEntity(
  @PrimaryKey override var id: ID = generateID,
  override var time: Long = 0,

  override var brokerId: ID = emptyID,
  var brokerStateStr: String = MatchState.Waiting.key,
  override var brokerNote: String = "",

  override var workerRequestId: ID = emptyID,
  var workerStateStr: String = MatchState.Waiting.key,
  override var workerNote: String = "",

  override var companyRequestId: ID = emptyID,
  var companyStateStr: String = MatchState.Waiting.key,
  override var companyNote: String = ""
) : BaseEntity(), LocalMatch {

  override var brokerState: MatchState
    get() = MatchState.from(brokerStateStr)
    set(brokerState) = let { it.brokerStateStr = brokerState.key }

  override var workerState: MatchState
    get() = MatchState.from(workerStateStr)
    set(workerState) = let { it.workerStateStr = workerState.key }

  override var companyState: MatchState
    get() = MatchState.from(companyStateStr)
    set(companyState) = let { it.companyStateStr = companyState.key }


  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<MatchEntity> {
    @Query("SELECT * FROM `Match`")
    fun list(): List<MatchEntity>

    @Query("DELETE FROM `Match` WHERE 1")
    fun clear()
  }
}

internal fun Match.toEntity() = MatchEntity(
  id = id,
  time = time,

  brokerId = brokerId,
  brokerStateStr = brokerState.key,
  brokerNote = brokerNote,

  workerRequestId = workerRequestId,
  workerStateStr = workerState.key,
  workerNote = workerNote,

  companyRequestId = companyRequestId,
  companyStateStr = companyState.key,
  companyNote = companyNote
)

internal fun <T : Match> Collection<T>.toEntity() = map { it.toEntity() }
