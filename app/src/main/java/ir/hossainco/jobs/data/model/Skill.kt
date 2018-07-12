package ir.hossainco.jobs.data.model

interface Skill {
  object EMPTY : Job {
    override val id = ""
    override val title = ""
  }

  val id: ID
	val title: String
}

interface SkillRequests {
	val skill: Skill
	val requests: List<Request>
}
