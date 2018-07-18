package ir.hossainkhademian.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog

object LogoutDialog {
  fun show(
    context: Context,
    onStay: () -> Unit,
    onLogout: () -> Unit
  ) =
    builder(context, onStay, onLogout).show()

  private fun builder(
    context: Context,
    onStay: () -> Unit,
    onLogout: () -> Unit
  ) = AlertDialog.Builder(context)
    .setCancelable(true)
    .setTitle("Are you sure you want to logout?")
    .setPositiveButton("Stay") { _, _ -> onStay() }
    .setNegativeButton("Logout") { _, _ -> onLogout() }
    .setOnCancelListener { onStay() }
}
