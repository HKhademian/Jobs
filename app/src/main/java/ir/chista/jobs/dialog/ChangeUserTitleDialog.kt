package ir.chista.jobs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.EditText
import ir.chista.jobs.R
import ir.chista.jobs.data.AccountManager
import android.text.InputType
//import org.jetbrains.anko.singleLine


object ChangeUserTitleDialog {
  fun show(
    context: Context,
    onCancel: () -> Unit,
    onChange: (String) -> Unit
  ) =
    builder(context, onCancel, onChange).show()

  private fun builder(
    context: Context,
    onCancel: () -> Unit,
    onChange: (String) -> Unit
  ) =
    EditText(context).also { editText ->
      editText.setText(AccountManager.user.title)
      editText.inputType = InputType.TYPE_CLASS_TEXT
      //editText.singleLine = true
    }.let { editText ->
      AlertDialog.Builder(context)
        .setCancelable(true)
        .setTitle(R.string.dialog_user_title_change_title)
        .setView(editText)
        .setPositiveButton(R.string.dialog_user_title_change_action_change) { _, _ -> onChange(editText.text.toString()) }
        .setNegativeButton(R.string.dialog_user_title_change_action_cancel) { _, _ -> onCancel() }
        .setOnCancelListener { onCancel() }
    }
}
