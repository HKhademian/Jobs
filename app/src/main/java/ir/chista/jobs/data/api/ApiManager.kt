package ir.chista.jobs.data.api

import ir.chista.jobs.App
import ir.chista.jobs.data.api.Services.initServices

internal object ApiManager {
  internal fun App.initApiManager() {
    initServices()
  }

  val accounts get() = Services.account
  val skills get() = Services.skill
  val jobs get() = Services.job
  val requests get() = Services.request
  val chats get() = Services.chat
  val users get() = Services.user
  val matches get() = Services.match
}
