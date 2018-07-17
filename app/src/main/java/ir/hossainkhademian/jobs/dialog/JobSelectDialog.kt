package ir.hossainkhademian.jobs.dialog

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.EmptyJob
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.Job
import ir.hossainkhademian.jobs.data.model.emptyID
import kotlinx.android.synthetic.main.item_dialog_job.view.*

object JobSelectDialog {
  fun show(context: Context, pastSelectionId: ID = emptyID, listener: (Job) -> Unit) =
    builder(context, pastSelectionId, listener).show()!!

  private fun builder(context: Context, pastSelectionId: ID = emptyID, listener: (Job) -> Unit): AlertDialog.Builder {
    val adapter = JobAdapter(context, pastSelectionId)
    val job = Repository.Jobs.list()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { adapter.items = it }

    return AlertDialog.Builder(context)
      .setTitle("Select job from list below:")
      .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
      .setOnDismissListener { job.dispose() }
      .setAdapter(adapter) { dialog, pos ->
        listener(adapter.getItem(pos))
        dialog.dismiss()
      }
  }

  private class JobAdapter(val context: Context, val pastSelectionId: ID = emptyID, items: List<Job> = emptyList()) : BaseAdapter() {
    var items = items
      set(items) {
        field = items
        notifyDataSetChanged()
      }

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = items.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      val view = convertView ?: LayoutInflater.from(context)
        .inflate(R.layout.item_dialog_job, parent, false)
      val item = getItem(position)
      val pastSelected = item.id == pastSelectionId

      view.jobView.job = item
      view.jobView.setCardBackgroundColor(if (pastSelected) Color.parseColor("#3666") else 0)

      return view
    }
  }
}
