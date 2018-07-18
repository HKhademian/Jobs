package ir.chista.jobs.data.api.model

import com.squareup.moshi.Json
import ir.chista.jobs.data.model.*

class JobMock(
  @Json(name = "id") override val id: ID = generateID,
  @Json(name = "title") override val title: String = ""
) : Job

fun JobMock.copy(
  id: ID? = null,
  title: String? = null
) = JobMock(
  id = id ?: this.id,
  title = title ?: this.title
)

fun Job.toMock() = JobMock(
  id = id,
  title = title
)

fun JobMock.toData() = JobData(
  id = id,
  title = title
)

fun Iterable<JobMock>.toData() = map { it.toData() }
fun <T : Job> Iterable<T>.toMock() = map { it.toMock() }

