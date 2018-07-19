package ir.chista.jobs.screen.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import ir.chista.jobs.R
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.data.AccountManager.isPhoneValid
import ir.chista.jobs.util.BaseActivity
import ir.chista.jobs.screen.login.LoginActivity
import ir.chista.util.launchActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class RegisterActivity : BaseActivity() {
  companion object {
    const val EXTRA_PHONE = "phone"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    super.onCreate(savedInstanceState)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    setContentView(R.layout.activity_register)
    login_button.isEnabled = true
    register_button.isEnabled = true
    register_form.visibility = View.VISIBLE
    wait_form.visibility = View.INVISIBLE
    phone_field.error = null

    handleIntent(intent)


    phone_field.setIconCallback {
      phone_field.setMaskedText("")
      phone_field.setSelection(4)
    }

    login_button.setOnClickListener {
      launchActivity<LoginActivity>(extras = *arrayOf(LoginActivity.EXTRA_PHONE to "+98${phone_field.unmaskedText}"))
    }

    register_button.setOnClickListener {
      phone_field.error = null
      val phone = "+98${phone_field.unmaskedText}"
      if (phone.length <= 3) {
        phone_field.error = getText(R.string.error_field_required)
        return@setOnClickListener
      }
      if (!isPhoneValid(phone)) {
        phone_field.error = getText(R.string.error_invalid_phone)
        return@setOnClickListener
      }
      register(phone)
    }

    background_image.startAnimation(AnimationUtils.loadAnimation(this, R.anim.jorney))
  }


  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent?) {
    val phone = intent?.getStringExtra(EXTRA_PHONE) ?: ""
    if (phone.length > 3)
      phone_field.setMaskedText(phone.takeLast(phone.length - 3)) /* -3 for +98 */
  }

  private fun register(phone: String) {
    val activity = this
    launch {
      val result = asyncUI {
        try {
          AccountManager.register(phone); null
        } catch (ex: Exception) {
          ex.printStackTrace(); ex
        }
      }.await()

      if (result == null) {
        Thread.sleep(1000) // cool down
        launch(UI) {
          Toast.makeText(activity, "Your password sent to your phone!\n\n" +
            "In development it's your last digit!", Toast.LENGTH_LONG).show()

          launchActivity<LoginActivity>(extras = *arrayOf(LoginActivity.EXTRA_PHONE to phone))
          finish()
        }
        return@launch
      }

      launch(UI) {
        Toast.makeText(activity, "Cannot register with this phone!\n${result.message}", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun <T> asyncUI(task: suspend () -> T) = async {
    launch(UI) {
      login_button.isEnabled = false
      register_button.isEnabled = false
      register_form.visibility = View.INVISIBLE
      wait_form.visibility = View.VISIBLE
    }

    val result = task.invoke()

    launch(UI) {
      login_button.isEnabled = true
      register_button.isEnabled = true
      register_form.visibility = View.VISIBLE
      wait_form.visibility = View.INVISIBLE
    }

    return@async result
  }
}
