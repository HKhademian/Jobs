package ir.chista.jobs.screen.request.detail

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.screen.chat.detail.ChatDetailActivity
import ir.chista.jobs.screen.chat.detail.ChatDetailFragment
import ir.chista.jobs.screen.request.edit.RequestEditActivity
import ir.chista.jobs.screen.request.edit.RequestEditFragment
import ir.chista.jobs.screen.request.list.RequestListActivity
import ir.chista.util.bundle
import ir.chista.util.context
import ir.chista.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_detail.*

class RequestDetailActivity : AppCompatActivity(), RequestDetailListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request_detail)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      handleIntent(intent)
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.let { handleIntent(it) }
  }

  private fun handleIntent(intent: Intent) {
    val requestId = intent.getStringExtra(RequestDetailFragment.ARG_REQUEST_ID) ?: emptyID

    val tag = RequestDetailFragment::class.java.simpleName
    val fragment = (supportFragmentManager.findFragmentByTag(tag) as? RequestDetailFragment)?.also { it.setRequestId(requestId) }
      ?: RequestDetailFragment().also { it.arguments = bundle(RequestDetailFragment.ARG_REQUEST_ID to requestId) }

    supportFragmentManager.beginTransaction()
      .replace(R.id.detailContainer, fragment, tag)
      .commit()
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        navigateUpTo(Intent(context, RequestListActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun onRequestDetailEdit(requestId: ID) {
    launchActivity<RequestEditActivity>(extras = *arrayOf(
      RequestEditFragment.ARG_REQUEST_ID to requestId
    ))
  }

  override fun onRequestDetailChat(requestId: ID, userId: ID) {
    if (userId.isNotEmpty)
      launchActivity<ChatDetailActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_CONTACT_ID to userId
      ))
  }

  override fun onRequestDetailCloseDone(requestId: ID) {
    Snackbar.make(toolbar, "Not Implemented!", Snackbar.LENGTH_SHORT).show()
    navigateUpTo(Intent(context, RequestListActivity::class.java))
    //finish()
  }
}
