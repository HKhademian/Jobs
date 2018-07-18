package ir.chista.jobs.screen.loading

import android.os.Bundle
import android.widget.Toast
import ir.chista.jobs.R
import ir.chista.jobs.data.DataManager
import ir.chista.jobs.dialog.LoadingErrorDialog
import ir.chista.jobs.util.BaseActivity
import ir.chista.jobs.screen.dashboard.DashboardActivity
import ir.chista.util.context
import ir.chista.util.launchActivity
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

      DataManager.loadOnlineData()
      delay(100) // a sweet wait
    }.invokeOnCompletion { e ->
      launch(UI) {
        if (e != null)
        else onLoadComplete()
      }
    }
  }

  private fun loadDatabase(retries: Int) {
    launch(CommonPool + CoroutineExceptionHandler { _, e ->
      launch(UI) { onLoadError(e, retries) }
    }) {
      DataManager.loadOfflineData()
      delay(100) // a sweet wait
    }.invokeOnCompletion { e ->
      launch(UI) {
        if (e != null)
        else onLoadComplete()
      }
    }
  }

  private fun modifyServer(retries: Int) {
    Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()

    loadServer(retries)
  }

  private fun exit(retries: Int) {
    Toast.makeText(context, "try men!", Toast.LENGTH_SHORT).show()
    loadServer(retries)
    // finish()
  }

  private fun onLoadError(e: Throwable, retries: Int) {
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
