@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "Request")
internal class RequestEntity(
  @PrimaryKey override var id: ID = emptyID,
  override var userId: ID = emptyID,
  var typeStr: String = RequestType.COMPANY.key,
  override var detail: String = "",
  override var time: Long = 0,
  override var jobId: ID = emptyID,
  var skillIdsStr: String = "",
  var brokerIdsStr: String = ""
) : BaseEntity(), LocalRequest {

  override var type: RequestType
    get() = RequestType.from(typeStr)
    set(type) = let { it.typeStr = type.key }

  override var skillIds: List<ID>
    get() = skillIdsStr.split(",")
    set(skillIds) = let { it.skillIdsStr = skillIds.joinToString(",") }

  override var brokerIds: List<ID>
    get() = brokerIdsStr.split(",")
    set(brokerIds) = let { it.brokerIdsStr = brokerIds.joinToString(",") }


  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<RequestEntity> {
    @Query("SELECT * FROM `Request`")
    fun list(): List<RequestEntity>

    @Query("DELETE FROM `Request` WHERE 1")
    fun clear()
  }
}

internal fun Request.toEntity() = RequestEntity(
  id = id,
  userId = userId,
  typeStr = type.key,
  detail = detail,
  time = time,
  jobId = jobId,
  skillIdsStr = skillIds.joinToString(","),
  brokerIdsStr = brokerIds.joinToString(",")
)

internal fun <T : Request> Collection<T>.toEntity() = map { it.toEntity() }
