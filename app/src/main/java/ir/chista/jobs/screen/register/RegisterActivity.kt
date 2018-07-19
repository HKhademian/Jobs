package ir.chista.jobs.screen.register

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.AnimationUtils
import ir.chista.jobs.R
import ir.chista.jobs.util.BaseActivity
import ir.chista.util.ViewModels.viewModel
import ir.chista.util.LiveDatas.observe
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
  companion object {
    const val EXTRA_PHONE = "phone"
  }

  private lateinit var viewModel: RegisterViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = viewModel()

    setContentView(R.layout.activity_register)

    phone_field.setIconCallback {
      phone_field.setMaskedText("")
      phone_field.setSelection(4)
    }

    register_button.setOnClickListener {
      phone_field.error = null
      title_field.error = null
      val phone = "+98${phone_field.unmaskedText}"
      val title = title_field.text.toString()
      viewModel.register(phone, title)
    }

    login_button.setOnClickListener {
      phone_field.error = null
      title_field.error = null
      val phone = "+98${phone_field.unmaskedText}"
      viewModel.login(phone)
    }

    background_image.startAnimation(AnimationUtils.loadAnimation(this, R.anim.jorney))


    viewModel.activity.observe(this) {
      val task = it.getContentIfNotHandled() ?: return@observe
      task.invoke(this)
    }

    viewModel.error.observe(this) {
      val ex = it.getContentIfNotHandled() ?: return@observe
      Snackbar.make(register_form, "an error occurs in Register screen:\n${ex.message ?: ex.toString()}\n\nif this happened many times, please contact support.", Snackbar.LENGTH_SHORT).show()
    }

    viewModel.phoneError.observe(this) {
      phone_field.error = if (it > 0) getText(it) else null
    }
    viewModel.titleError.observe(this) {
      title_field.error = if (it > 0) getText(it) else null
    }
    viewModel.isSending.observe(this) { isSending ->
      login_button.isEnabled = !isSending
      register_button.isEnabled = !isSending
      register_form.visibility = if (isSending) View.INVISIBLE else View.VISIBLE
      wait_form.visibility = if (isSending) View.VISIBLE else View.GONE
    }

    if (savedInstanceState == null) {
      viewModel.init()
      handleIntent(intent)
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent?) {
    val phone = intent?.getStringExtra(EXTRA_PHONE) ?: ""
    if (phone.length > 3) {
      val unmaskedPhone = phone.takeLast(phone.length - 3)
      if (unmaskedPhone != phone_field.unmaskedText) {
        phone_field.setMaskedText(phone.takeLast(phone.length - 3)) /* -3 for +98 */
        title_field.text.clear()
      }
    }
  }
}
