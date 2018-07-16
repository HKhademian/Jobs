package ir.hossainkhademian.jobs.data.database

import android.arch.persistence.room.Room
import ir.hossainkhademian.jobs.App

object DatabaseRepository {
  internal lateinit var db: AppDatabase
    private set

  private val userDao get() = db.userDao()
  private val chatDao get() = db.chatDao()
  private val jobDao get() = db.jobDao()

  internal fun App.initDatabaseManager() {
    db = Room
      .databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
      .build()
  }

//  fun App.mockDatabaseManager() {
//    db = Room
//      .databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
//      .addCallback(object : RoomDatabase.Callback() {
//        override fun onOpen(db: SupportSQLiteDatabase) {
//          super.onOpen(db)
//          createMockData()
//        }
//      })
//      .build()
//  }

//  private fun App.createMockData() = launch(CommonPool) {
//    val rand = Random()

//		db.runInTransaction {
//			db.clearAllTables()
//			(1..(20 + (rand.nextLong() % 20))).forEach {
//				val user = UserEntity(0, "User #$it")
//				val requestId = userDao.insert(user)
//				(1..(0 + (rand.nextLong() % 15))).forEach {
//					chatDao.insert(ChatEntity(0, requestId, rand.nextBoolean(), "Message Body #${rand.nextInt()}", rand.nextLong()))
//				}
//			}
//		}


//    try {
//      val sd = Environment.getExternalStorageDirectory()
//      if (sd.canWrite()) {
//        val currentDBPath = "/data/data/$packageName/databases/database.db"
//        val backupDBPath = "database.db"
//        val currentDB = File(currentDBPath)
//        val backupDB = File(sd, backupDBPath)
//
//        if (currentDB.exists()) {
//          backupDB.delete()
//          val src = FileInputStream(currentDB).channel
//          val dst = FileOutputStream(backupDB).channel
//          dst.transferFrom(src, 0, src.size())
//          src.close()
//          dst.close()
//        }
//      }
//    } catch (e: Exception) {
//      e.printStackTrace()
//    }
//  }

//  fun listsByContact(requestId: ID): LiveData<List<Chat>> {
//    return chatDao.listByContact(requestId) as LiveData<List<Chat>>
//  }
//
//  fun listUserChats(): LiveData<List<UserWithChats>> {
//    return chatDao.listUserChats() as LiveData<List<UserWithChats>>
//  }
//
//  fun getJobsList(): LiveData<List<Job>> {
//    return jobDao.list() as LiveData<List<Job>>
//  }
}
