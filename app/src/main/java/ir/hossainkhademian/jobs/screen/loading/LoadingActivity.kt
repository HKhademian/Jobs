package ir.hossainkhademian.jobs.screen.loading

import android.os.Bundle
import android.widget.Toast
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.DataManager
import ir.hossainkhademian.jobs.dialog.LoadingErrorDialog
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.dashboard.DashboardActivity
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.io.IOException


class LoadingActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_loading)

    loadServer()
  }

  private fun loadServer(retries: Int = 0) {
    launch(CommonPool + CoroutineExceptionHandler { _, e ->
      launch(UI) { onLoadError(e, retries) }
    }) {
      if (retries < 1) {
        delay(1500)
        throw IOException("a fake Exception to test the flow")
      }

      DataManager.loadOnlineData(context)
      delay(100) // a sweet wait
    }.invokeOnCompletion { e ->
      launch(UI) {
        if (e != null)
        else onLoadComplete()
      }
    }
  }

  private fun loadDatabase(retries: Int) {
    Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()

    loadServer(retries)
  }

  private fun modifyServer(retries: Int) {
    Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()

    loadServer(retries)
  }

  private fun exit(retries: Int) {
    finish()
  }

  private fun onLoadError(e: Throwable, retries: Int) {
    //val snack = Snackbar.make(constraint, "Error while loading: \n${e.message}", Snackbar.LENGTH_INDEFINITE)
    //snack.setAction("OK") { snack.dismiss() }
    //snack.show()

    LoadingErrorDialog.show(
      context, e, retries,
      loadServer = ::loadServer,
      loadDatabase = ::loadDatabase,
      modifyServer = ::modifyServer,
      exit = ::exit
    )
  }

  private fun onLoadComplete() {
    // if (AccountManager.isLoggedIn)
    launchActivity<DashboardActivity>()
    // else
    //  launchActivity<RegisterActivity>()

    finish()
  }
}
