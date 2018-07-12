package ir.hossainco.jobs.data.model

interface Job {
  object EMPTY : Job {
    override val id = ""
    override val title = ""
  }

  val id: ID
  val title: String
}
