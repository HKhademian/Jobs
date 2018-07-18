package ir.hossainkhademian.util

import android.app.Activity
import android.app.Notification
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.vdurmont.emoji.EmojiManager
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime
import ir.hossainkhademian.util.Collections.pickRandom
import ir.hossainkhademian.util.Collections.random
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context.NOTIFICATION_SERVICE
import android.os.ResultReceiver
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService


object Texts {
  private val emojis = arrayOf(
    "😀", "🤣", "😅", "😊", "😍", "😘", "😋", "😆", "😃", "😁", "😂", "😄", "😉", "😎", "😗", "🎈", "🎆", "🎇", "🎊", "🎉", "✨", "🎃", "🎄", "🎋", "🎏", "🎎", "🎍", "🎐", "🎑", "🎀", "🎁", "🎪", "🎨", "🎭", "🍕", "🍔", "🍟", "🥓", "🍿", "🌭", "🥚", "🥞", "🍳", "🥨", "🥐", "🍞", "🥖", "🧀", "🥗", "🌮", "🥪", "🥙", "🌯", "🥫"
  )
  //  val emojiPattern = "/[\u2190-\u21FF]|[\u2600-\u26FF]|[\u2700-\u27BF]|[\u3000-\u303F]|[\u1F300-\u1F64F]|[\u1F680-\u1F6FF]/g".toRegex()
//  val String.isEmoji get() = matches(emojyPattern)
//  val String.containsEmoji get() = contains(emojyPattern)
  val String.isEmoji get() = EmojiManager.isOnlyEmojis(this)
  val String.containsEmoji get() = EmojiManager.isEmoji(this)

  fun Long.getRelativeTime(context: Context) =
    DateUtils.getRelativeTimeSpanString(context, DateTime(this))

  fun Activity.hideKeyboard() = try {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
  } catch (ex: Exception) {
  }

  val randomEmoji
    get() = emojis[random.nextInt(emojis.size)]

  fun getRandomEmojis(n: Int = 10) =
    emojis.pickRandom(n, n)

  fun getRandomEmojis(min: Int = 1, max: Int = 10) =
    emojis.pickRandom(min, max)

  fun fastNotify(context: Context, title: String, message: String) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(context, ResultReceiver::class.java)
    val pIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
    val notification = NotificationCompat.Builder(context)
      .setContentTitle(title)
      .setContentText(message)
      .setContentIntent(pIntent)
      .setAutoCancel(true)
      .setTicker("JOBS")
      .setSmallIcon(android.R.drawable.sym_def_app_icon)
      .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
      .build()
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
  }
}
