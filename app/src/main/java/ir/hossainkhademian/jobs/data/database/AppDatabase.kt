package ir.hossainkhademian.jobs.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [UserEntity::class, ChatEntity::class, JobEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserEntity.Dao
  abstract fun chatDao(): ChatEntity.Dao
  abstract fun jobDao(): JobEntity.Dao
}
