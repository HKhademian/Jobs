package ir.hossainkhademian.jobs.screen.request.edit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.context
import kotlinx.android.synthetic.main.activity_request_edit.*

class RequestEditActivity : AppCompatActivity(), RequestEditListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_request_edit)

    // Show the Up button in the action bar.
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      //val requestId = intent.getStringExtra(RequestEditFragment.ARG_REQUEST_ID) ?: emptyID
      //val title = intent.getStringExtra(RequestEditFragment.ARG_REQUEST_TITLE) ?: ""

      val fragment = RequestEditFragment().apply {
        arguments = intent.extras // forward
      }

      //toolbar.title = title

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

  override fun onRequestEditSubmit(request: Request, type: RequestType, job: Job, skills: Collection<Skill>, detail: String) {
//    if (request.isNotEmpty)
//      launchActivity<RequestEditActivity>(extras = *arrayOf(
//        RequestEditFragment.ARG_REQUEST_ID to requestId
//      ))
  }

  override fun onRequestEditCancel(request: Request) {
    //finish()
    navigateUpTo(Intent(context, RequestListActivity::class.java))
  }
}
