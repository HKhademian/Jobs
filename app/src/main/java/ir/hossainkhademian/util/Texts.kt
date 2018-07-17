package ir.hossainkhademian.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.vdurmont.emoji.EmojiManager
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime

object Texts {
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
}
