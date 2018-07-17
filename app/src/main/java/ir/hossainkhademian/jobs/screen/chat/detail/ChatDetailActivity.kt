package ir.hossainkhademian.jobs.screen.chat.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import kotlinx.android.synthetic.main.activity_chat_detail.*

class ChatDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_detail)

    // Show the Up button in the action bar.
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      val userId = intent.getStringExtra(ChatDetailFragment.ARG_USER_ID) ?: emptyID
      val title = intent.getStringExtra(ChatDetailFragment.ARG_USER_TITLE) ?: ""

      //Picasso.get().list(generateAvatarUrl(requestId)).placeholder(R.drawable.ic_avatar).into(avatar_view)

      val fragment = ChatDetailFragment().apply {
        arguments = intent.extras/* bundle(
          RequestEditFragment.ARG_REQUEST_ID to requestId,
          RequestEditFragment.ARG_REQUEST_TITLE to title
        )*/
      }

      toolbar.title = title

      supportFragmentManager.beginTransaction()
        .replace(R.id.detailContainer, fragment)
        .commit()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        navigateUpTo(Intent(this, ChatListActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
}
