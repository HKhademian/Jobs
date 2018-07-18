package ir.hossainkhademian.jobs.dialog

import android.content.Context
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import ir.hossainkhademian.jobs.R
import kotlinx.android.synthetic.main.fragment_waiting.view.*
import java.lang.ref.WeakReference


object WaitDialog {
  private val tag = WaitDialog::class.java.simpleName

  fun dismiss(activity: FragmentActivity) {
    val supportFragmentManager = activity.supportFragmentManager
    val fragment = supportFragmentManager.findFragmentByTag(tag) as? WaitingDialogFragment
    // fragment?.let { supportFragmentManager.beginTransaction().remove(it).commit() }
    fragment?.listener?.clear()
    fragment?.dismiss()
  }

  fun show(activity: FragmentActivity, listener: WaitDialogListener? = null): DialogFragment {
    val supportFragmentManager = activity.supportFragmentManager
    val fragment =
      (supportFragmentManager.findFragmentByTag(tag) as? WaitingDialogFragment)?.also { supportFragmentManager.beginTransaction().remove(it).commit() }
        ?: builder()

    fragment.listener = WeakReference(listener)
    fragment.show(supportFragmentManager, tag)

    return fragment
  }

  private fun builder() =
    WaitingDialogFragment()

  internal class WaitingDialogFragment : DialogFragment() {
    private lateinit var rootView: View
    var listener: WeakReference<WaitDialogListener?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      rootView = inflater.inflate(R.layout.fragment_waiting, container, true)
      dialog.requestWindowFeature(STYLE_NO_TITLE)
      dialog.setCancelable(false)
      return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      val listener = listener?.get() ?: activity as? WaitDialogListener
      rootView.cancelAction.setOnClickListener { listener?.onWaitDialogClose(this) }
      rootView.cancelAction.visibility = if (listener != null) View.VISIBLE else View.GONE
    }
  }

  interface WaitDialogListener {
    fun onWaitDialogClose(dialog: DialogFragment)
  }
}
