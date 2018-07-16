@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException
import java.util.*

object ChatMock : ChatService {
  override fun list(accessToken: String): Call<List<ChatData>> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    return Calls.response(MockApiStorage.chats.items.filterByContactId(user.id).toData())
  }

  override fun list(accessToken: String, contactId: ID): Call<List<ChatData>> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    MockApiStorage.users.items.findById(contactId)
      ?: return Calls.failure(IOException("user with that contactId is not found"))

    return Calls.response(MockApiStorage.chats.items.filterByUserContactId(user.id, contactId).toData())
  }

  override fun send(accessToken: String, contactId: ID, message: String): Call<List<ChatData>> {
    MockApiStorage.fakeWait(0)

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val contact = MockApiStorage.users.items.findById(contactId)
      ?: return Calls.failure(IOException("no user with this contactId found"))

    if (message.contains("error"))
      return Calls.failure(IOException("error in sending message as you job lord"))

    val chats = arrayListOf<ChatData>()

    let {
      val chat = ChatData(
        senderId = user.id,
        receiverId = contactId,
        message = message
      )
      chats += chat
      MockApiStorage.chats.update(chat)
      // return Calls.response(chat)
    }

    if (message.contains("answer")) {
      val chat = ChatData(
        senderId = contactId,
        receiverId = user.id,
        message = "my answer lord: \n" + MockApiStorage.lorem.getWords(30, 100)
      )
      chats += chat
      MockApiStorage.chats.update(chat)
    }

    return Calls.response(chats)
  }

  override fun markAsSeen(accessToken: String, senderId: ID): Call<List<ChatData>> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val sender = MockApiStorage.users.items.findById(senderId)
      ?: return Calls.failure(IOException("user with this senderId not found. please relogin"))

    val chats = MockApiStorage.chats.items
      .filterByReceiverId(user.id)
      .filterBySenderId(senderId)
      .map { it.copy(unseen = false) }

    chats.forEach { MockApiStorage.chats.update(it) }

    return Calls.response(chats)
  }
}
