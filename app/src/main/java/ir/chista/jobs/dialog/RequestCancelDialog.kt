package ir.chista.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import ir.chista.jobs.R
import ir.chista.jobs.data.model.Request

object RequestCancelDialog {
  fun <T : Request> show(context: Context, item: T, onCancel: (T) -> Unit) =
    builder(context, item, onCancel).show()!!

  private fun <T : Request> builder(context: Context, item: T, onCancel: (T) -> Unit): AlertDialog.Builder {
    return AlertDialog.Builder(context)
      .setTitle(R.string.dialog_job_cancel_title)
      .setMessage(R.string.dialog_job_cancel_message)
      .setNegativeButton(R.string.dialog_job_cancel_action_cancel) { _, _ -> onCancel(item) }
      .setPositiveButton(R.string.dialog_job_cancel_action_keep) { _, _ ->  }
  }
}
