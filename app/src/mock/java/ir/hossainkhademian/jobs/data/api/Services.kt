package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.App

internal object Services {
  val account = AccountMock
  val skill = SkillMock
  val job = JobMock
  val request = RequestMock
  val chat = ChatMock
  val user = UserMock
  val match = MatchMock

  internal fun App.initServices() {
    MockApiStorage.initMockApiStorage(applicationContext)
  }
}
