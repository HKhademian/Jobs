package ir.chista.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import ir.chista.jobs.R

object BrokerRemoveDialog {
  fun show(
    context: Context,
    onStay: () -> Unit,
    onRemove: () -> Unit
  ) =
    builder(context, onStay, onRemove).show()

  private fun builder(
    context: Context,
    onStay: () -> Unit,
    onRemove: () -> Unit
  ) = AlertDialog.Builder(context)
    .setCancelable(true)
    .setTitle(R.string.dialog_remove_broker_title)
    .setPositiveButton(R.string.dialog_remove_broker_action_stay) { _, _ -> onStay() }
    .setNegativeButton(R.string.dialog_remove_broker_action_remove) { _, _ -> onRemove() }
    .setOnCancelListener { onStay() }
}
