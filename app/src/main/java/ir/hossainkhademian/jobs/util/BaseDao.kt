package ir.hossainkhademian.jobs.util

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update

@Dao
interface BaseDao<in T : BaseEntity> {
  @Insert
  fun insert(item: T): Long

  @Insert
  fun insertAll(vararg items: T): List<Long>

  @Insert
  fun insertAll(items: List<T>): List<Long>

  @Update
  fun update(item: T): Int

  @Update
  fun updateAll(vararg items: T): Int

  @Update
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
