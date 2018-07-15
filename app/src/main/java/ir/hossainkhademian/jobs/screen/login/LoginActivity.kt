package ir.hossainkhademian.jobs.screen.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.data.AccountManager.isPasswordValid
import ir.hossainkhademian.jobs.data.AccountManager.isPhoneValid
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.dashboard.DashboardActivity
import ir.hossainkhademian.jobs.screen.loading.LoadingActivity
import ir.hossainkhademian.jobs.screen.register.RegisterActivity
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import ir.hossainkhademian.util.Collections.tri
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.sdk25.coroutines.onClick

class LoginActivity : BaseActivity() {
  companion object {
    const val EXTRA_PHONE = "phone"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    super.onCreate(savedInstanceState)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    setContentView(R.layout.activity_login)

    login_button.isEnabled = true
    register_button.isEnabled = true
    login_form.visibility = View.VISIBLE
    wait_form.visibility = View.INVISIBLE
    phone_field.error = null
    password_field.error = null


    handleIntent(intent)


    phone_field.setIconCallback {
      phone_field.setMaskedText("")
      phone_field.setSelection(4)
    }

    register_button.setOnClickListener {
      launchActivity<RegisterActivity>(extras = *arrayOf(RegisterActivity.EXTRA_PHONE to "+98${phone_field.unmaskedText}"))
    }

    login_button.onClick {
      phone_field.error = null
      password_field.error = null
      val phone = "+98${phone_field.unmaskedText}"
      val password = password_field.text.toString()

      if (phone.length <= 3) {
        phone_field.error = getText(R.string.error_field_required)
        return@onClick
      }
      if (!isPhoneValid(phone)) {
        phone_field.error = getText(R.string.error_invalid_phone)
        return@onClick
      }
      if (password.isBlank()) {
        password_field.error = getText(R.string.error_field_required)
        return@onClick
      }
      if (!isPasswordValid(password)) {
        password_field.error = getText(R.string.error_incorrect_password)
        return@onClick
      }
      login(phone, password)
    }

    background_image.startAnimation(AnimationUtils.loadAnimation(this, R.anim.jorney))
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent?) {
    val phone = intent?.getStringExtra(EXTRA_PHONE) ?: ""
    if (phone.length > 3) {
      phone_field.setMaskedText(phone.takeLast(phone.length - 3)) /* -3 for +98 */
      password_field.text.clear()
    }
  }

  private fun login(phone: String, password: String) {
    val activity = this
    launch {
      val result = asyncUI {
        tri(false) { AccountManager.login(context, phone, password); true }
      }.await()

      if (result) {
        delay(100) // cool down
        launch(UI) {
          // launchActivity<DashboardActivity>()
          launchActivity<LoadingActivity>()
          finish()
        }
        return@launch
      }

      launch(UI) {
        Toast.makeText(activity, "Wrong Phone & Password combination!", Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun <T> asyncUI(task: suspend () -> T) = async {
    launch(UI) {
      login_button.isEnabled = false
      register_button.isEnabled = false
      login_form.visibility = View.INVISIBLE
      wait_form.visibility = View.VISIBLE
    }

    val result = task.invoke()

    launch(UI) {
      login_button.isEnabled = true
      register_button.isEnabled = true
      login_form.visibility = View.VISIBLE
      wait_form.visibility = View.INVISIBLE
    }

    return@async result
  }
}
