package ir.chista.jobs.data.api

import ir.chista.jobs.App

object Services {
  val account = AccountMockService
  val skill = SkillMockService
  val job = JobMockService
  val request = RequestMockService
  val chat = ChatMockService
  val user = UserMockService
  val match = MatchMockService

  fun App.initServices() {
    MockApiStorage.initMockApiStorage(applicationContext)
  }
}
