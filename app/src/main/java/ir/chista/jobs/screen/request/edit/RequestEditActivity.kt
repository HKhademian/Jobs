package ir.chista.jobs.screen.request.edit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.screen.request.detail.RequestDetailActivity
import ir.chista.jobs.screen.request.detail.RequestDetailFragment
import ir.chista.jobs.screen.request.list.RequestListActivity
import ir.chista.util.bundle
import ir.chista.util.context
import ir.chista.util.launchActivity
import ir.chista.util.putExtras
import kotlinx.android.synthetic.main.activity_request_edit.*

class RequestEditActivity : AppCompatActivity(), RequestEditListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request_edit)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    //fabDetail?.visibility = View.GONE

    if (savedInstanceState == null) {
      handleIntent(intent)
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.let { handleIntent(it) }
  }

  private fun handleIntent(intent: Intent) {
    val requestId = intent.getStringExtra(RequestEditFragment.ARG_REQUEST_ID) ?: emptyID

    val tag = RequestEditFragment::class.java.simpleName
    val fragment =
      (supportFragmentManager.findFragmentByTag(tag) as? RequestEditFragment)?.also { it.setRequestId(requestId) }
        ?: RequestEditFragment().also { it.arguments = bundle(RequestEditFragment.ARG_REQUEST_ID to requestId) }

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

  override fun onRequestEditDone(requestId: ID) {
    // navigateUpTo(Intent(context, RequestListActivity::class.java))

     launchActivity<RequestDetailActivity>(extras = *arrayOf(RequestDetailFragment.ARG_REQUEST_ID to requestId))
     finish()

    //navigateUpTo(Intent(context, RequestDetailActivity::class.java).also {
    //  it.putExtras(bundle(
    //    RequestDetailFragment.ARG_REQUEST_ID to requestId
    //  ))
    //})
  }

  override fun onRequestEditCancel(requestId: ID) {
    //finish()
    navigateUpTo(Intent(context, RequestListActivity::class.java))
  }
}
