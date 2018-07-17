package ir.hossainkhademian.jobs.data.api.model

import com.squareup.moshi.Json
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.data.model.Skill
import ir.hossainkhademian.jobs.data.model.SkillData


class SkillMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") val title: String = ""
) : IdModel

fun SkillMock.copy(
  id: ID? = null,
  title: String? = null
) = UserMock(
  id = id ?: this.id,
  title = title ?: this.title
)

fun SkillMock.toData() =  SkillData(
  id = id,
  title = title
)

fun Skill.toMock() =  SkillData(
  id = id,
  title = title
)

fun Iterable<SkillMock>.toData() = map { it.toData() }
fun <T : Skill> Iterable<T>.toMock() = map { it.toMock() }
