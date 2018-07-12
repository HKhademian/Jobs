@file:Suppress("PackageDirectoryMismatch")

package ir.hossainco.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.hossainco.jobs.data.model.ID
import ir.hossainco.jobs.data.model.Job
import ir.hossainco.jobs.util.BaseDao
import ir.hossainco.jobs.util.BaseEntity

@Entity(tableName = "Job")
internal class JobEntity(
    @PrimaryKey override val id: ID,
    override val title: String
) : BaseEntity(), Job {

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<JobEntity> {
    @Query("SELECT * FROM `Job`")
    fun list(): LiveData<List<JobEntity>>
  }
}
