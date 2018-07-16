package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json

private fun generateAvatarUrl(userId: ID): String {
  val i = userId.sumBy { it.toInt() } // requestId.toInt() //
  val index = i % 10
  return "https://randomuser.me/api/portraits/lego/$index.jpg"
}

val EmptySkill = SkillData(id = emptyID)
val Skill.avatarUrl get() = generateAvatarUrl(id)

interface Skill : IdModel {
  override val id: ID
  val title: String
}

class SkillData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = ""
) : Skill

fun Skill.toData() = when (this) {
  is SkillData -> this
  else -> SkillData(
    id = id,
    title = title
  )
}

fun <T : Skill> Iterable<T>.toData() = map { it.toData() }
