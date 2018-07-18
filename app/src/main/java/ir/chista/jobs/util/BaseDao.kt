package ir.chista.jobs.util

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import android.database.sqlite.SQLiteDatabase

@Dao
interface BaseDao<in T : BaseEntity> {
  @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
  fun insert(item: T): Long

  @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
  fun insertAll(vararg items: T): List<Long>

  @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
  fun insertAll(items: List<T>): List<Long>

  @Update(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
  fun update(item: T): Int

  @Update(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
  fun updateAll(vararg items: T): Int

  @Update(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
  fun updateAll(items: List<T>): Int

  @Delete
  fun delete(item: T): Int

  @Delete
  fun deleteAll(vararg items: T): Int

  @Delete
  fun deleteAll(items: List<T>): Int

  //@Delete
  //abstract fun clear()
}
