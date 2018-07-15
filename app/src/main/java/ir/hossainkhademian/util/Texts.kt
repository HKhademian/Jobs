package ir.hossainkhademian.util

import com.vdurmont.emoji.EmojiManager

object Texts {
//  val emojiPattern = "/[\u2190-\u21FF]|[\u2600-\u26FF]|[\u2700-\u27BF]|[\u3000-\u303F]|[\u1F300-\u1F64F]|[\u1F680-\u1F6FF]/g".toRegex()
//  val String.isEmoji get() = matches(emojyPattern)
//  val String.containsEmoji get() = contains(emojyPattern)
  val String.isEmoji get() = EmojiManager.isOnlyEmojis(this)
  val String.containsEmoji get() = EmojiManager.isEmoji(this)
}
