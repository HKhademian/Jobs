package ir.chista.jobs.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [UserEntity::class, ChatEntity::class,
  JobEntity::class, SkillEntity::class, RequestEntity::class,
  MatchEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserEntity.Dao
  abstract fun chatDao(): ChatEntity.Dao
  abstract fun jobDao(): JobEntity.Dao
  abstract fun skillDao(): SkillEntity.Dao
  abstract fun requestDao(): RequestEntity.Dao
  abstract fun matchDao(): MatchEntity.Dao
}
