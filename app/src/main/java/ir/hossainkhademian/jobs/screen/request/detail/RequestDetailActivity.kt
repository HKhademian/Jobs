package ir.hossainkhademian.jobs.screen.request.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailActivity
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailFragment
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditActivity
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditFragment
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.bundle
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_detail.*

class RequestDetailActivity : AppCompatActivity(), RequestDetailListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request_detail)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      val requestId = intent.getStringExtra(RequestDetailFragment.ARG_REQUEST_ID) ?: emptyID

      val fragment = RequestDetailFragment().apply {
        arguments = bundle(RequestDetailFragment.ARG_REQUEST_ID to requestId)
      }

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
    if (user.isNotEmpty)
      launchActivity<ChatDetailActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_CONTACT_ID to user.id
      ))
  }

  override fun onRequestDetailCancel(request: Request) {
    //finish()
    navigateUpTo(Intent(context, RequestListActivity::class.java))
  }
}
