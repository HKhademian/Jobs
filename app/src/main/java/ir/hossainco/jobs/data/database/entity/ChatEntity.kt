@file:Suppress("PackageDirectoryMismatch")

package ir.hossainco.jobs.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ir.hossainco.jobs.data.model.Chat
import ir.hossainco.jobs.data.model.ID
import ir.hossainco.jobs.data.model.UserWithChats
import ir.hossainco.jobs.util.BaseDao
import ir.hossainco.jobs.util.BaseEntity

@Entity(tableName = "Chat")
internal class ChatEntity(
    @PrimaryKey override var id: ID,
    override var contactId: ID,
    override var direction: Chat.Direction,
    override var text: String,
    override var time: Long
) : BaseEntity(), Chat {

  class UserWithChatsEntity : UserWithChats {
    @Embedded
    override lateinit var user: UserEntity

    @Relation(parentColumn = "id", entityColumn = "contactId", entity = ChatEntity::class)
    override lateinit var chats: List<ChatEntity>
  }

  @android.arch.persistence.room.Dao
  interface Dao : BaseDao<ChatEntity> {
    @Query("SELECT * FROM `Chat` ORDER BY `time`")
    fun list(): LiveData<List<ChatEntity>>

    @Query("SELECT `id` FROM `Chat` ORDER BY `time`")
    fun listIds(): LiveData<List<Long>>

    @Query("SELECT * FROM `Chat` WHERE `id` = :id ORDER BY `time`")
    fun getById(id: Long): LiveData<List<ChatEntity>>

    @Query("SELECT * FROM `Chat` WHERE `contactId` = :contactId ORDER BY `time`")
    fun listByContact(contactId: Long): LiveData<List<ChatEntity>>

    @Query("SELECT DISTINCT `contactId` FROM `Chat` ORDER BY `time`")
    fun listUniqueContactIds(): LiveData<List<Long>>

    @Query("SELECT * FROM `User`")
    fun listUserChats(): LiveData<List<UserWithChatsEntity>>
  }
}
