package ir.chista.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog

object LoadingErrorDialog {
  private const val MAX_RETRY_COUNT = 5

  fun show(
    context: Context,
    e: Throwable,
    retries: Int,
    loadServer: (Int) -> Unit,
    loadDatabase: (Int) -> Unit,
    modifyServer: (Int) -> Unit,
    exit: (Int) -> Unit
  ) =
    builder(context, e, retries, loadServer, loadDatabase, modifyServer, exit).show()

  private fun builder(
    context: Context,
    e: Throwable,
    retries: Int,
    loadServer: (Int) -> Unit,
    loadDatabase: (Int) -> Unit,
    modifyServer: (Int) -> Unit,
    exit: (Int) -> Unit
  ) = AlertDialog.Builder(context)
    .setCancelable(false)
    .setTitle("Error in loading data")
    .setMessage("We have some problems in receiving data:\n\n*< ${e.message ?: e.toString()} >*\n\nif this happens many times please contact support team!")
    .apply {

      setNeutralButton("explore offline") { _, _ -> loadDatabase(retries + 1) }

      if (retries >= MAX_RETRY_COUNT) {
        setNegativeButton("Exit") { _, _ -> exit(retries + 1) }
      } else {
        setPositiveButton("Refresh") { _, _ -> loadServer(retries + 1) }

        // if (BuildConfig.DEBUG && !BuildConfig.MOCK)
        setNegativeButton("modify Server address") { _, _ -> modifyServer(retries + 1) }
      }
    }
}
