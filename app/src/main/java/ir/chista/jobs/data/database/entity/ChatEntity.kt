@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseDao
import ir.chista.jobs.util.BaseEntity

@Entity(tableName = "Chat")
internal class ChatEntity(
  @PrimaryKey override var id: ID = generateID,
  override var senderId: ID = emptyID,
  override var receiverId: ID = emptyID,
  override var message: String = "",
  override var unseen: Boolean = true,
  override var time: Long = System.currentTimeMillis()
) : BaseEntity(), LocalChat {
  @Ignore
  override val seen = !unseen

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<ChatEntity> {
    @Query("SELECT * FROM `Chat` ORDER BY `time`")
    fun list(): List<ChatEntity>

    @Query("DELETE FROM `Chat` WHERE 1")
    fun clear(): Unit
  }
}

internal fun Chat.toEntity() = ChatEntity(
  id = id,
  senderId = senderId,
  receiverId = receiverId,
  message = message,
  unseen = unseen,
  time = time
)

internal fun <T : Chat> Collection<T>.toEntity() = map { it.toEntity() }
