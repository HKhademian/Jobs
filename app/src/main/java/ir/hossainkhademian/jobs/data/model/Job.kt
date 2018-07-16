package ir.hossainkhademian.jobs.data.model

import com.squareup.moshi.Json

private fun generateAvatarUrl(id: ID): String {
  val i = id.sumBy { it.toInt() } // requestId.toInt() //
  val index = i % 10
  return "https://randomuser.me/api/portraits/lego/$index.jpg"
}

val EmptyJob: Job = JobData(id = emptyID)
val Job.avatarUrl get() = generateAvatarUrl(id)

interface Job : IdModel {
  override val id: ID
  val title: String
}


class JobData(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = ""
) : Job

fun <T : Job> Iterable<T>.toData() = map { it.toData() }

fun Job.toData() = when (this) {
  is JobData -> this
  else -> JobData(
    id = id,
    title = title
  )
}
