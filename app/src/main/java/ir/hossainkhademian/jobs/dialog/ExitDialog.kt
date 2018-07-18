package ir.hossainkhademian.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog

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
    .setCancelable(false)
    .setTitle("Are you sure you want to exit?")
    .setMessage("Please rate us if you like this App!")
    .setPositiveButton("Stay") { _, _ -> onStay() }
    .setNeutralButton("Rate") { _, _ -> onRate() }
    .setNegativeButton("Exit") { _, _ -> onExit() }
}
