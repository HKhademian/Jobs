package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json

private fun generateAvatarUrl(userId: ID): String {
  val i = userId.sumBy { it.toInt() } // userId.toInt() //
  val index = i % 50
  val gender = if (i % 2 == 0) "women" else "men"
  return "https://randomuser.me/api/portraits/$gender/$index.jpg"
}

val EmptyJob = JobData(id = emptyID)
val Job.avatarUrl get() = generateAvatarUrl(id)

interface Job : IdModel {
  override val id: ID
  val title: String
}


class JobData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = ""
) : Job

fun Job.toData() = when (this) {
  is JobData -> this
  else -> JobData(
    id = id,
    title = title
  )
}
