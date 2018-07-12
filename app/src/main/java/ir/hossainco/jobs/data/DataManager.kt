package ir.hossainco.jobs.data

import android.arch.lifecycle.LiveData
import ir.hossainco.jobs.App
import ir.hossainco.jobs.data.AccountManager.initAccountManager
import ir.hossainco.jobs.data.api.ApiRepository.initApiManager
import ir.hossainco.jobs.data.database.DatabaseRepository
import ir.hossainco.jobs.data.database.DatabaseRepository.initDatabaseManager
import ir.hossainco.jobs.data.model.*

object DataManager {
  var jobs: List<Job> = emptyList()
    private set

  var skills: List<Skill> = emptyList()
    private set

  var users: List<User> = emptyList()
    private set

  var requests: List<Request> = emptyList()
    private set

  var chats: List<Chat> = emptyList()
    private set

  fun App.initDataManager() {
    initAccountManager()
    initDatabaseManager()
    initApiManager()
  }

  fun getChatsByContact(userId: Long): LiveData<List<Chat>> {
    return DatabaseRepository.getChatsByContact(userId)
  }

  fun getUserChats(): LiveData<List<UserWithChats>> {
    return DatabaseRepository.getUserChats()
  }

  fun getJobs(): LiveData<List<Job>> {
    return DatabaseRepository.getJobs()
  }
}
