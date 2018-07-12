package ir.hossainco.jobs.screen.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.hossainco.jobs.R
import kotlinx.android.synthetic.main.activity_chat_detail.*

/**
 * An activity representing a single Chat detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ChatListActivity].
 */
class ChatDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat_detail)

		// Show the Up button in the action bar.
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		if (savedInstanceState == null) {
			val userId = intent.getLongExtra(ChatDetailFragment.ARG_USER_ID, 0)
			val title = intent.getStringExtra(ChatDetailFragment.ARG_USER_TITLE)

			//Picasso.get().load(generateAvatarUrl(userId)).placeholder(R.drawable.ic_avatar).into(avatar_view)

			val fragment = ChatDetailFragment().apply {
				arguments = Bundle().apply {
					putLong(ChatDetailFragment.ARG_USER_ID, userId)
					putString(ChatDetailFragment.ARG_USER_TITLE, title)
				}
			}

			toolbar.title = title

			supportFragmentManager.beginTransaction()
				.replace(R.id.chat_detail_container, fragment)
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
