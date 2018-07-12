package ir.hossainco.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.os.Environment
import ir.hossainco.jobs.App
import ir.hossainco.jobs.data.Repository
import ir.hossainco.jobs.data.model.Chat
import ir.hossainco.jobs.data.model.Job
import ir.hossainco.jobs.data.model.UserWithChats
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object DatabaseRepository : Repository {
	internal lateinit var db: AppDatabase
		private set

	private val userDao get() = db.userDao()
	private val chatDao get() = db.chatDao()
	private val jobDao get() = db.jobDao()

	fun App.initDatabaseManager() {
		db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
			.addCallback(object : RoomDatabase.Callback() {
				override fun onOpen(db: SupportSQLiteDatabase) {
					super.onOpen(db)
					createMockData()
				}
			})
			.build()
	}

	private fun App.createMockData() = launch(CommonPool) {
		val rand = Random()

//		db.runInTransaction {
//			db.clearAllTables()
//			(1..(20 + (rand.nextLong() % 20))).forEach {
//				val user = UserEntity(0, "User #$it")
//				val userId = userDao.insert(user)
//				(1..(0 + (rand.nextLong() % 15))).forEach {
//					chatDao.insert(ChatEntity(0, userId, rand.nextBoolean(), "Message Body #${rand.nextInt()}", rand.nextLong()))
//				}
//			}
//		}


		try {
			val sd = Environment.getExternalStorageDirectory()
			if (sd.canWrite()) {
				val currentDBPath = "/data/data/$packageName/databases/database.db"
				val backupDBPath = "database.db"
				val currentDB = File(currentDBPath)
				val backupDB = File(sd, backupDBPath)

				if (currentDB.exists()) {
					backupDB.delete()
					val src = FileInputStream(currentDB).channel
					val dst = FileOutputStream(backupDB).channel
					dst.transferFrom(src, 0, src.size())
					src.close()
					dst.close()
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun getChatsByContact(userId: Long): LiveData<List<Chat>> {
		return chatDao.listByContact(userId) as LiveData<List<Chat>>
	}

	override fun getUserChats(): LiveData<List<UserWithChats>> {
		return chatDao.listUserChats() as LiveData<List<UserWithChats>>
	}

	override fun getJobs(): LiveData<List<Job>> {
		return jobDao.list() as LiveData<List<Job>>
	}
}
