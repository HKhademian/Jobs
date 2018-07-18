@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.LocalSkill
import ir.chista.jobs.data.model.Skill
import ir.chista.jobs.data.model.generateID
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "Skill")
internal class SkillEntity(
  @PrimaryKey override var id: ID = generateID,
  override var title: String = ""
) : BaseEntity(), LocalSkill {

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<SkillEntity> {
    @Query("SELECT * FROM `Skill`")
    fun list(): List<SkillEntity>

    @Query("DELETE FROM `Skill` WHERE 1")
    fun clear()
  }
}

internal fun Skill.toEntity() = SkillEntity(
  id = id,
  title = title
)

internal fun <T : Skill> Collection<T>.toEntity() = map { it.toEntity() }
