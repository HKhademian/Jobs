package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.App

internal object Services {
  val account = AccountMockService
  val skill = SkillMockService
  val job = JobMockService
  val request = RequestMockService
  val chat = ChatMockService
  val user = UserMockService
  val match = MatchMockService

  internal fun App.initServices() {
    MockApiStorage.initMockApiStorage(applicationContext)
  }
}
