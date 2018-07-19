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
import ir.chista.jobs.data.model.User
import kotlinx.android.synthetic.main.item_dialog_user.view.*

object BrokerSelectDialog {
  fun show(context: Context, selectedIds: Collection<ID> = emptyList(), onSelect: (User) -> Unit) =
    builder(context, selectedIds, onSelect).show()!!

  private fun builder(context: Context, selectedIds: Collection<ID>, onSelect: (User) -> Unit): AlertDialog.Builder {
    val adapter = UsersAdapter(context, selectedIds)
    val job = Repository.Users.listBrokers()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { adapter.items = it }

    return AlertDialog.Builder(context)
      .setTitle("Select broker from list below:")
      .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
      .setOnDismissListener { job.dispose() }
      .setOnCancelListener { job.dispose() }
      .setAdapter(adapter) { _, pos ->
        val item = adapter.getItem(pos)
        if (isItemSelected(item.id, selectedIds)) {
          Toast.makeText(context, "You can't select item!", Toast.LENGTH_SHORT).show()
        } else {
          onSelect(item)
        }
      }
  }

  private fun isItemSelected(itemId: ID, selectedIds: Collection<ID>) =
    selectedIds.contains(itemId)

  private class UsersAdapter(val context: Context, val selectedIds: Collection<ID>, items: List<User> = emptyList()) : BaseAdapter() {
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
        .inflate(R.layout.item_dialog_user, parent, false)
      val item = getItem(position)
      val isSelected = isItemSelected(item.id, selectedIds)

      view.userView.user = item
      view.userView.setCardBackgroundColor(
        if (isSelected) context.resources.getColor(R.color.colorSelector)
        else context.resources.getColor(R.color.colorIcons))

      return view
    }
  }
}
