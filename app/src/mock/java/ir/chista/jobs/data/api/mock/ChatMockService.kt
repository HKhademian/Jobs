@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.api.model.ChatMock
import ir.chista.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException
import ir.chista.util.Texts.isEmoji

object ChatMockService : ChatService {
  private val emojis = listOf("❤", "😊", "😎", "😁", "🌹", "👍", "😒", "🎁", "👏")

  override fun list(accessToken: String): Call<List<ChatData>> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    @Suppress("WhenWithOnlyElse")
    val chats = when (login.role) {
      //UserRole.Admin -> MockApiStorage.chats.items
      else -> MockApiStorage.chats.items.filterByContactId(login.id)
    }

    return Calls.response(chats.toData())
  }

  override fun list(accessToken: String, contactId: ID): Call<List<ChatData>> {
    return Calls.failure(IOException("Deprected API"))

    //MockApiStorage.fakeWait()
    //
    //val login = MockApiStorage.getUserByAccessToken(accessToken)
    //  ?: return Calls.failure(IOException("user with this token not found. please relogin"))
    //
    //MockApiStorage.users.items.findById(contactId)
    //  ?: return Calls.failure(IOException("user with that contactId is not found"))
    //
    //return Calls.response(MockApiStorage.chats.items.filterByUserContactId(login.id, contactId).toData())
  }

  override fun send(accessToken: String, contactId: ID, message: String): Call<List<ChatData>> {
    MockApiStorage.fakeWait(0)

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val contact = MockApiStorage.users.items.findById(contactId)
      ?: return Calls.failure(IOException("no user with this contactId found"))

    if (message.contains("error"))
      return Calls.failure(IOException("error in sending message as you job lord"))

    val chats = arrayListOf<ChatMock>()

    let {
      val chat = ChatMock(
        senderId = login.id,
        receiverId = contactId,
        message = message
      )
      chats += chat
      MockApiStorage.chats.update(chat)
      // return Calls.response(chat)
    }

    if (message.contains("answer")) {
      val chat = ChatMock(
        senderId = contactId,
        receiverId = login.id,
        message = "my answer lord: \n" + MockApiStorage.lorem.getWords(30, 100),
        time = chats[0].time + 100
      )
      chats += chat
      MockApiStorage.chats.update(chat)
    }

    if (message.isEmoji) {
      val chat = ChatMock(
        senderId = contactId,
        receiverId = login.id,
        message = emojis.shuffled().first(),
        time = chats[0].time + 100
      )
      chats += chat
      MockApiStorage.chats.update(chat)
    }

    return Calls.response(chats.toData())
  }

  override fun markAsSeen(accessToken: String, senderId: ID): Call<List<ChatData>> {
    MockApiStorage.fakeWait()

    val login = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val sender = MockApiStorage.users.items.findById(senderId)
      ?: return Calls.failure(IOException("user with this senderId not found. please relogin"))

    val chats = MockApiStorage.chats.items
      .filterByReceiverId(login.id)
      .filterBySenderId(senderId)
      .filter { it.unseen }
      .map { it.copy(unseen = false) }

    chats.forEach { MockApiStorage.chats.update(it) }

    return Calls.response(chats.toData())
  }
}
