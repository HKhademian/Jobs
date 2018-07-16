package ir.hossainkhademian.jobs.screen.request.edit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.jobs.data.model.idStr
import ir.hossainkhademian.jobs.data.model.isEmpty
import ir.hossainkhademian.jobs.data.model.title
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailFragment
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_edit.*

class RequestEditActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request_edit)

    // Show the Up button in the action bar.
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      val requestId = intent.getStringExtra(RequestEditFragment.ARG_REQUEST_ID) ?: emptyID
      val title = intent.getStringExtra(RequestEditFragment.ARG_REQUEST_TITLE) ?: ""

      val fragment = RequestEditFragment().apply {
        arguments = intent.extras

        onSubmitListener = { requestId ->
          if (requestId != emptyID)
            launchActivity<RequestEditActivity>(extras = *arrayOf(
              RequestEditFragment.ARG_REQUEST_ID to requestId
            ))
        }

        onCancelListener = { requestId ->
          //finish()
          navigateUpTo(Intent(context, RequestListActivity::class.java))
        }
      }

      toolbar.title = title

      supportFragmentManager.beginTransaction()
        .replace(R.id.request_detail_container, fragment)
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
}
