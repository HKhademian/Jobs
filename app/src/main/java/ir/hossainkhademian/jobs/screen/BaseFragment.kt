package ir.hossainkhademian.jobs.screen

import android.support.v4.app.Fragment
import ir.hossainkhademian.jobs.App

abstract class BaseFragment : Fragment() {
  val app get() = activity!!.application as App
}
