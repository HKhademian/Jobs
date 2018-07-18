package ir.chista.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import ir.chista.jobs.R
import ir.chista.jobs.data.model.Request

object JobCancelDialog {
  fun <T : Request> show(context: Context, item: T, listener: (T) -> Unit) =
    builder(context, item, listener).show()!!

  private fun <T : Request> builder(context: Context, item: T, listener: (T) -> Unit): AlertDialog.Builder {
    return AlertDialog.Builder(context)
      .setTitle("Job Cancellation")
      .setMessage("Are you sure to cancel job request?\nattention this action is Irreversible!")
      .setNegativeButton("Cancel") { dialog, _ -> listener(item); dialog.dismiss() }
      .setPositiveButton("Keep") { dialog, _ -> dialog.dismiss() }
  }
}
