@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.Job
import ir.hossainkhademian.jobs.util.BaseDao
import ir.hossainkhademian.jobs.util.BaseEntity

@Entity(tableName = "Job")
internal class JobEntity(
  @PrimaryKey override var id: ID = "",
  override var title: String = ""
) : BaseEntity(), Job {

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<JobEntity> {
    @Query("SELECT * FROM `Job`")
    fun list(): LiveData<List<JobEntity>>
  }
}
