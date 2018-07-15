@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.Skill
import ir.hossainkhademian.jobs.util.BaseDao
import ir.hossainkhademian.jobs.util.BaseEntity

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
