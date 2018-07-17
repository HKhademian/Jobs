package ir.hossainkhademian.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.Job
import ir.hossainkhademian.jobs.data.model.Request
import kotlinx.android.synthetic.main.item_dialog_job.view.*

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
