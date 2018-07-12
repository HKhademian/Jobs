package ir.hossainco.jobs.screen.register

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import ir.hossainco.jobs.R
import ir.hossainco.jobs.data.AccountManager
import ir.hossainco.jobs.data.AccountManager.isPhoneValid
import ir.hossainco.jobs.screen.BaseActivity
import ir.hossainco.jobs.screen.login.LoginActivity
import ir.hossainco.jobs.util.context
import ir.hossainco.jobs.util.launchActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.sdk25.coroutines.onClick

class RegisterActivity : BaseActivity() {
	companion object {
		const val EXTRA_PHONE = "phone"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
		super.onCreate(savedInstanceState)
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

		AlertDialog.Builder(context).setTitle("MOCK !!!")
			.setMessage("In Test mode:\n\n" +
				"* any phone number ...\n" +
				"ends with 0 is Admin\n" +
				"end with 1 is Broker\n" +
				"and other are normal users (workers & companies).\n\n" +
				"so Admin and Brokers are not allowed to register in mobile app.\n\n" +
				"account password is last phone number digit!!\n\n" +
				"any number is valid to login now till server developed.")
			.setNeutralButton("OK !", { dialog, which -> dialog.cancel() })
			.setCancelable(false)
			.show()

		setContentView(R.layout.activity_register)
		login_button.isEnabled = true
		register_button.isEnabled = true
		register_form.visibility = View.VISIBLE
		wait_form.visibility = View.INVISIBLE
		phone_field.error = null


		handleIntent(intent)


		phone_field.setIconCallback { phone_field.setMaskedText("") }

		login_button.setOnClickListener {
			launchActivity<LoginActivity>(extras = *arrayOf(LoginActivity.EXTRA_PHONE to "+98${phone_field.unmaskedText}"))
		}

		register_button.onClick {
			phone_field.error = null
			val phone = "+98${phone_field.unmaskedText}"
			if (phone.length <= 3) {
				phone_field.error = getText(R.string.error_field_required)
				return@onClick
			}
			if (!isPhoneValid(phone)) {
				phone_field.error = getText(R.string.error_invalid_phone)
				return@onClick
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
		launch(CommonPool) {
			val result = asyncUI {
				return@asyncUI AccountManager.register(phone)
			}.await()

			if (result) {
				Thread.sleep(100) // cool down
				launch(UI) {
					Toast.makeText(activity, "Your password sent to your phone!\n\n" +
						"In development it's your last digit!", Toast.LENGTH_LONG).show()
					launchActivity<LoginActivity>(extras = *arrayOf(LoginActivity.EXTRA_PHONE to phone))
					finish()
				}
				return@launch
			}

			launch(UI) {
				Toast.makeText(activity, "Cannot register with this phone!\n" +
					"because last digit is 0!", Toast.LENGTH_LONG).show()
			}
		}
	}

	private fun <T> asyncUI(task: suspend () -> T) = async(CommonPool) {
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
