package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json

private fun generateAvatarUrl(id: ID): String {
  return "https://api.adorable.io/avatars/256/$id.png"
}

val EmptySkill = SkillData(id = emptyID)
val Skill.avatarUrl get() = generateAvatarUrl(id)

interface Skill : IdModel {
  override val id: ID
  val title: String
}

interface LocalSkill : Skill

class SkillData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = ""
) : LocalSkill

fun Skill.toData() = when (this) {
  is SkillData -> this
  else -> SkillData(
    id = id,
    title = title
  )
}

fun <T : Skill> Iterable<T>.toData() = map { it.toData() }
