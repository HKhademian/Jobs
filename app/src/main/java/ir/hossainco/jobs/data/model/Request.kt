package ir.hossainco.jobs.data.model

import ir.hossainco.jobs.data.DataManager

interface Request {
  enum class Type(val key: String) {
    WORKER("worker"), COMPANY("company");

    companion object {
      fun from(direction: String) = when (direction.toLowerCase()) {
        WORKER.key.toLowerCase() -> WORKER
        COMPANY.key.toLowerCase() -> COMPANY
        else -> throw RuntimeException("bad value")
      }
    }
  }

  val id: ID
  val type: Type
  val detail: String

  val jobId: ID
  val skillIds: List<ID>

  val job get() = DataManager.jobs.firstOrNull { it.id == jobId } ?: Job.EMPTY
  val skills get() = DataManager.skills.filter { skillIds.contains(it.id) }
}

interface RequestSkills {
  val request: Request
  val skills: List<Skill>
}
