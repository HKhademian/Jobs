@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.Skill
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "Skill")
internal class SkillEntity(
  @PrimaryKey override var id: ID = "",
  override var title: String = ""
) : BaseEntity(), Skill {

  @android.arch.persistence.room.Dao
  abstract class Dao : BaseDao<SkillEntity> {
    @Query("SELECT * FROM `Skill`")
    abstract fun list(): LiveData<List<SkillEntity>>
  }
}
