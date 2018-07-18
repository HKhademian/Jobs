@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.Job
import ir.chista.jobs.data.model.LocalJob
import ir.chista.jobs.data.model.generateID
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "Job")
internal class JobEntity(
  @PrimaryKey override var id: ID = generateID,
  override var title: String = ""
) : BaseEntity(), LocalJob {

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<JobEntity> {
    @Query("SELECT * FROM `Job`")
    fun list(): List<JobEntity>

    @Query("DELETE FROM `Job` WHERE 1")
    fun clear()
  }
}

internal fun Job.toEntity() = JobEntity(
  id = id,
  title = title
)

internal fun <T : Job> Collection<T>.toEntity() = map { it.toEntity() }
