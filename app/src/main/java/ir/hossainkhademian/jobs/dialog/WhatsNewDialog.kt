package ir.hossainkhademian.jobs.dialog

import io.github.tonnyl.whatsnew.item.item
import io.github.tonnyl.whatsnew.item.whatsNew
import ir.hossainkhademian.jobs.R

object WhatsNewDialog {
  fun create() = whatsNew {
    item {
      title = "Complete chat section"
      content = "Now you can freely chat with Admins, Brokers & known Users (from your request match)\n" +
        "you can have fun with EMOJI's fantastic UX!"
      imageRes = R.drawable.ic_action_emoji
    }
    item {
      title = "Job Requests!"
      content = "Now you can explore, send & edit Job request\n" +
        "whether send as a worker or as an employer (we say it Company 😉)"
      imageRes = R.drawable.ic_action_star
    }
    item {
      title = "managed Jobs and Skills"
      content = "As this point forward, we only categories Requests in know Jobs\n" +
        "also we add skills to tag your request for better Visibility 🎉"
      imageRes = R.drawable.ic_action_media
    }
    item {
      title = "Mock update news 👍"
      content = "this is a mock message, please forgive me. I just want to fill the blanks 😎"
      imageRes = R.drawable.ic_action_delete
    }
    item {
      title = "another one 😢"
      content = "sorry its urgent"
    }
    item {
      title = "and this new update fake message"
      content = "ok this one is the last, have fun 🤞"
    }
  }
}
