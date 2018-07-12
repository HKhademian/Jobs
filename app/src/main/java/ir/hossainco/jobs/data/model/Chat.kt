package ir.hossainco.jobs.data.model

interface Chat {
  enum class Direction(val key: String) {
    SEND("send"), RECEIVE("receive");

    companion object {
      fun from(direction: String) = when (direction.toLowerCase()) {
        SEND.key.toLowerCase() -> SEND
        RECEIVE.key.toLowerCase() -> RECEIVE
        else -> throw RuntimeException("bad value")
      }
    }
  }

  val id: ID
  val contactId: ID
  val direction: Direction
  val text: String
  val time: Long
}
