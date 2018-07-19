package ir.chista.jobs.data.database

import android.arch.persistence.room.Room
import ir.chista.jobs.App

internal object DatabaseManager {
  internal lateinit var db: AppDatabase
    private set

  val userDao get() = db.userDao()
  val chatDao get() = db.chatDao()
  val jobDao get() = db.jobDao()
  val skillDao get() = db.skillDao()
  val requestDao get() = db.requestDao()
  val matchDao get() = db.matchDao()

  internal fun App.initDatabaseManager() {
    db = Room
      .databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
      .build()
  }
}
