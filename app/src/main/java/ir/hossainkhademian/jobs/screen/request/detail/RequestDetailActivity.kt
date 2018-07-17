package ir.hossainkhademian.jobs.screen.request.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailFragment
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditActivity
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditFragment
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditListener
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_detail.*

class RequestDetailActivity : AppCompatActivity(), RequestDetailListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request_detail)

    // Show the Up button_accent in the action bar.
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      // val requestId = intent.getStringExtra(RequestDetailFragment.ARG_REQUEST_ID) ?: emptyID
      // val title = intent.getStringExtra(RequestDetailFragment.ARG_REQUEST_TITLE) ?: ""

      val fragment = RequestDetailFragment().apply {
        arguments = intent.extras // forward
      }

      // toolbar.title = title

      supportFragmentManager.beginTransaction()
        .replace(R.id.detailContainer, fragment)
        .commit()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        navigateUpTo(Intent(context, RequestListActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun onRequestDetailEdit(request: Request) {
    launchActivity<RequestEditActivity>(extras = *arrayOf(
      RequestEditFragment.ARG_REQUEST_ID to request.id
    ))
  }

  override fun onRequestDetailChat(request: Request, user: User) {
    if(user.isNotEmpty)
      launchActivity<ChatListActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_USER_ID to user.id,
        ChatDetailFragment.ARG_SINGLE_PANEL to true
      ))
  }

  override fun onRequestDetailCancel(request: Request) {
    //finish()
    navigateUpTo(Intent(context, RequestListActivity::class.java))
  }
}
