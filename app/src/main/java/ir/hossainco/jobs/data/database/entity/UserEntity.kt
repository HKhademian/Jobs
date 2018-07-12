@file:Suppress("PackageDirectoryMismatch")

package ir.hossainco.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.hossainco.jobs.data.model.ID
import ir.hossainco.jobs.data.model.User
import ir.hossainco.jobs.util.BaseDao
import ir.hossainco.jobs.util.BaseEntity

@Entity(tableName = "User")
internal class UserEntity(
    @PrimaryKey override var id: ID,
    override var title: String,
    override var lastSeen: Long,
    override var role: User.Role
) : BaseEntity(), User {

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<UserEntity> {
    @Query("SELECT * FROM `User`")
    fun list(): LiveData<List<UserEntity>>

    @Query("SELECT `id` FROM `User`")
    fun listIds(): LiveData<List<Long>>

//	@Query("SELECT * FROM `User` WHERE `id` IN :ids")
//	abstract fun listById(vararg ids: Long): LiveData<List<UserEntity>>

    @Query("SELECT * FROM `User` WHERE `id` = :id")
    fun getById(id: Long): LiveData<List<UserEntity>>
  }
}
