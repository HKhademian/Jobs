package ir.chista.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import ir.chista.jobs.R

object ExitDialog {
  fun show(
    context: Context,
    onStay: () -> Unit,
    onRate: () -> Unit,
    onExit: () -> Unit
  ) =
    builder(context, onStay, onRate, onExit).show()

  private fun builder(
    context: Context,
    onStay: () -> Unit,
    onRate: () -> Unit,
    onExit: () -> Unit
  ) = AlertDialog.Builder(context)
    .setCancelable(true)
    .setTitle(R.string.dialog_exit_title)
    .setMessage(R.string.dialog_exit_message)
    .setPositiveButton(R.string.dialog_exit_action_stay) { _, _ -> onStay() }
    .setNeutralButton(R.string.dialog_exit_action_rate) { _, _ -> onRate() }
    .setNegativeButton(R.string.dialog_exit_action_exit) { _, _ -> onExit() }
    .setOnCancelListener { onStay() }
}
