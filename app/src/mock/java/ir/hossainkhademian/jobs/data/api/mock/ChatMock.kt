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

    return Calls.response(MockApiStorage.chats.filterByContactId(user.id).map { it.toData() })
  }

  override fun list(accessToken: String, contactId: ID): Call<List<ChatData>> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    MockApiStorage.users.findById(contactId)
      ?: return Calls.failure(IOException("user with that contactId is not found"))

    return Calls.response(MockApiStorage.chats.filterByUserContactId(user.id, contactId).map { it.toData() })
  }

  override fun send(accessToken: String, contactId: ID, message: String): Call<List<ChatData>> {
    MockApiStorage.fakeWait(0)

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val contact = MockApiStorage.users.findById(contactId)
      ?: return Calls.failure(IOException("no user with this contactId found"))

    if (message.contains("error"))
      return Calls.failure(IOException("error in sending message as you request lord"))

    val chats = arrayListOf<ChatData>()

    let {
      val chat = ChatData(
        senderId = user.id,
        receiverId = contactId,
        message = message
      )
      chats += chat
      MockApiStorage.update(chat)
      // return Calls.response(chat)
    }

    if (message.contains("answer")) {
      val chat = ChatData(
        senderId = contactId,
        receiverId = user.id,
        message = "my answer lord: \n" + MockApiStorage.lorem.getWords(30, 100)
      )
      chats += chat
      MockApiStorage.update(chat)
    }

//    val chance = 25
//    if (MockApiStorage.random.nextInt(100) < chance) {
//      val msg = "a fake messageField from `${contact.title}`" +
//        "to respond your messageField,\n" +
//        "with $chance% chance to send.\n" +
//        "includes a random generated body:\n" +
//        MockApiStorage.lorem.getWords(20, 50)
//      val chat = ChatData(
//        senderId = contactId,
//        receiverId = user.id,
//        message = msg
//      )
//      chats += chat
//      MockApiStorage.update(chat)
//    }

    return Calls.response(chats)
  }

  override fun markAsSeen(accessToken: String, chatId: ID): Call<ChatData> {
    MockApiStorage.fakeWait()

    val user = MockApiStorage.getUserByAccessToken(accessToken)
      ?: return Calls.failure(IOException("user with this token not found. please relogin"))

    val oldChat = MockApiStorage.chats.findById(chatId)
      ?: return Calls.failure(IOException("no chat with this id found"))

    if (oldChat.receiverId != user.id)
      return Calls.failure(IOException("user is not chat's sender"))

    val chat = ChatData(
      id = oldChat.id,
      senderId = oldChat.senderId,
      receiverId = oldChat.receiverId,
      message = oldChat.message,
      unseen = false,
      time = oldChat.time
    )
    MockApiStorage.update(chat)

    return Calls.response(chat)
  }
}
