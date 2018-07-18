package ir.chista.jobs.dialog

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import ir.chista.jobs.R
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.Job
import ir.chista.jobs.data.model.emptyID
import kotlinx.android.synthetic.main.item_dialog_job.view.*

object JobSelectDialog {
  fun show(context: Context, selectedId: ID = emptyID, listener: (Job) -> Unit) =
    builder(context, selectedId, listener).show()!!

  private fun builder(context: Context, selectedId: ID = emptyID, listener: (Job) -> Unit): AlertDialog.Builder {
    val adapter = JobAdapter(context, selectedId)
    val job = Repository.Jobs.list()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { adapter.items = it }

    return AlertDialog.Builder(context)
      .setTitle("Select job from list below:")
      .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
      .setOnDismissListener { job.dispose() }
      .setOnCancelListener { job.dispose() }
      .setAdapter(adapter) { _, pos ->
        val item = adapter.getItem(pos)
        if (isItemSelected(item.id, selectedId)) {
          Toast.makeText(context, "You can't select item!", Toast.LENGTH_SHORT).show()
        } else {
          listener(item)
        }
      }
  }

  private fun isItemSelected(itemId: ID, selectedId: ID) =
    selectedId == itemId

  private class JobAdapter(val context: Context, val selectedId: ID = emptyID, items: List<Job> = emptyList()) : BaseAdapter() {
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
      val isSelected = isItemSelected(item.id, selectedId)

      view.jobView.job = item
      view.jobView.setCardBackgroundColor(
        if (isSelected) context.resources.getColor(R.color.colorSelector)
        else context.resources.getColor(R.color.colorIcons))

      return view
    }
  }
}
