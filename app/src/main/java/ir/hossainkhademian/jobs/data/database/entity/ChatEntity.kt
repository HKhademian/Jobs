@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import ir.hossainkhademian.jobs.data.model.Chat
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.jobs.data.model.generateID
import ir.hossainkhademian.jobs.util.BaseDao
import ir.hossainkhademian.jobs.util.BaseEntity

@Entity(tableName = "Chat")
internal class ChatEntity(
  @PrimaryKey override var id: ID = generateID,
  override var senderId: ID = emptyID,
  override var receiverId: ID = emptyID,
  override var message: String = "",
  override var unseen: Boolean = true,
  override var time: Long = System.currentTimeMillis()
) : BaseEntity(), Chat {
//
//  class UserWithChatsEntity : UserWithChats {
//    @Embedded
//    override lateinit var user: UserEntity
//
//    @Relation(parentColumn = "id", entityColumn = "contactId", entity = ChatEntity::class)
//    override lateinit var userChats: List<ChatEntity>
//  }

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<ChatEntity> {
    @Query("SELECT * FROM `Chat` ORDER BY `time`")
    fun list(): LiveData<List<ChatEntity>>

    @Query("SELECT `id` FROM `Chat` ORDER BY `time`")
    fun listIds(): LiveData<List<ID>>

    @Query("SELECT * FROM `Chat` WHERE `id` = :id ORDER BY `time`")
    fun getById(id: ID): LiveData<List<ChatEntity>>

    @Query("SELECT * FROM `Chat` WHERE `senderId` = :userId OR `receiverId` = :userId ORDER BY `time`")
    fun listByContact(userId: ID): LiveData<List<ChatEntity>>

    @Query("SELECT DISTINCT `senderId` AND `receiverId` FROM `Chat` ORDER BY `time`")
    fun listUniqueContactIds(): LiveData<List<ID>>

//    @Query("SELECT * FROM `User`")
//    fun listUserChats(): LiveData<List<UserWithChatsEntity>>
  }
}
