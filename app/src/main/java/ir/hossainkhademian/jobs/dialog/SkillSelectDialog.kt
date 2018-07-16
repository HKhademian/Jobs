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
import ir.hossainkhademian.jobs.data.model.Skill
import kotlinx.android.synthetic.main.item_dialog_skill.view.*

object SkillSelectDialog {
  fun show(context: Context, listener: (Skill) -> Unit) =
    build(context, listener).show()!!

  fun build(context: Context, listener: (Skill) -> Unit): AlertDialog.Builder {
    val adapter = SkillAdapter(context)
    val job = Repository.skillsObservable
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

  private class SkillAdapter(val context: Context, items: List<Skill> = emptyList()) : BaseAdapter() {
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

       view.skillView.skill = getItem(position)

      return view
    }
  }
}
