package ir.hossainkhademian.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog

object LoadingErrorDialog {
  private const val MAX_RETRY_COUNT = 3

  fun show(
    context: Context,
    e: Throwable,
    retries: Int,
    loadServer: (Int) -> Unit,
    loadDatabase: (Int) -> Unit,
    modifyServer: (Int) -> Unit,
    exit: (Int) -> Unit
  ) =
    build(context, e, retries, loadServer, loadDatabase, modifyServer, exit).show()

  fun build(
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
    .setMessage("We have some problems in receiving data:\n($e)\n\nif this happens many times please contact support team!")
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
