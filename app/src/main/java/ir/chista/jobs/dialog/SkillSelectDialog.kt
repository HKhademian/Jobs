package ir.chista.jobs.dialog

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import ir.chista.jobs.R
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.Skill
import kotlinx.android.synthetic.main.item_dialog_skill.view.*

object SkillSelectDialog {
  fun show(context: Context, selectedIds: Collection<ID> = emptyList(), listener: (Skill) -> Unit) =
    builder(context, selectedIds, listener).show()!!

  private fun builder(context: Context, selectedIds: Collection<ID> = emptyList(), listener: (Skill) -> Unit): AlertDialog.Builder {
    val adapter = SkillAdapter(context, selectedIds)
    val job = Repository.Skills.list()
      .subscribe {
        adapter.items = it!!
        // .filterNot { selectedIds.contains(it.id) }
      }
    return AlertDialog.Builder(context)
      .setTitle(R.string.dialog_skill_select_title)
      .setNegativeButton(R.string.dialog_skill_select_action_cancel) { _, _ -> }
      .setOnDismissListener { job.dispose() }
      .setOnCancelListener { job.dispose() }
      .setAdapter(adapter) { _, pos ->
        val item = adapter.getItem(pos)
        if (isItemSelected(item.id, selectedIds)) {
          Toast.makeText(context, R.string.dialog_skill_select_alert_rapeat, Toast.LENGTH_SHORT).show()
        } else {
          listener(item)
        }
      }
  }

  private fun isItemSelected(itemId: ID, selectedIds: Collection<ID>) =
    selectedIds.contains(itemId)

  private class SkillAdapter(val context: Context, val selectedIds: Collection<ID> = emptyList(), items: List<Skill> = emptyList()) : BaseAdapter() {
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
        .inflate(R.layout.item_dialog_skill, parent, false)
      val item = getItem(position)
      val isSelected = isItemSelected(item.id, selectedIds)

      view.skillView.skill = item
      view.skillView.setCardBackgroundColor(
        if (isSelected) context.resources.getColor(R.color.colorSelector)
        else context.resources.getColor(R.color.colorIcons))

      return view
    }
  }
}
