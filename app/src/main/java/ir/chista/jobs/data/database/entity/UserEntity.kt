@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "User")
internal class UserEntity(
  @PrimaryKey override var id: ID = generateID,
  override var title: String = "",
  override var lastSeen: Long = 0L,
  var roleStr: String = ""
) : BaseEntity(), LocalUser {

  override var role: UserRole
    get() = UserRole.from(roleStr)
    set(value) = let { it.roleStr = value.key }

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<UserEntity> {
    @Query("SELECT * FROM `User`")
    fun list(): List<UserEntity>

    @Query("DELETE FROM `User` WHERE 1")
    fun clear()
  }
}

internal fun User.toEntity() = UserEntity(
  id = id,
  title = title,
  lastSeen = lastSeen,
  roleStr = role.key
)

internal fun <T : User> Collection<T>.toEntity() = map { it.toEntity() }
