@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.User
import ir.hossainkhademian.jobs.data.model.UserRole
import ir.hossainkhademian.jobs.data.model.generateID
import ir.hossainkhademian.jobs.util.BaseDao
import ir.hossainkhademian.jobs.util.BaseEntity

@Entity(tableName = "User")
internal class UserEntity(
  @PrimaryKey override var id: ID = generateID,
  override var title: String = "",
  override var lastSeen: Long = 0L,
  var roleStr: String = ""
) : BaseEntity(), User {
  override var role: UserRole
    get() = UserRole.from(roleStr)
    set(value) = Unit.apply { roleStr = value.key }

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<UserEntity> {
    @Query("SELECT * FROM `User`")
    fun list(): LiveData<List<UserEntity>>

    @Query("SELECT `id` FROM `User`")
    fun listIds(): LiveData<List<ID>>

//	@Query("SELECT * FROM `User` WHERE `id` IN :ids")
//	abstract fun listById(vararg ids: Long): LiveData<List<UserEntity>>

    @Query("SELECT * FROM `User` WHERE `id` = :id")
    fun getById(id: Long): LiveData<List<UserEntity>>
  }
}
