package ir.hossainkhademian.jobs.screen

import android.support.v4.app.Fragment
import ir.hossainkhademian.jobs.App

abstract class BaseFragment : Fragment() {
  companion object {
    const val ARG_ID = "id"
    const val ARG_TITLE = "title"
  }

  val app get() = activity!!.application as App
}
