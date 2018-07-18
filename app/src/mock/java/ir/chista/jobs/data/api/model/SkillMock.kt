package ir.chista.jobs.data.api.model

import com.squareup.moshi.Json
import ir.chista.jobs.data.model.*
import ir.chista.jobs.data.model.Skill
import ir.chista.jobs.data.model.SkillData


class SkillMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = ""
) : Skill

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
