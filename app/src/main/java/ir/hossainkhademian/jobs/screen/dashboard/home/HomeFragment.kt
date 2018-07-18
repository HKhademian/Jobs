package ir.hossainkhademian.jobs.screen.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.AccountManager
import ir.hossainkhademian.jobs.screen.BaseFragment
import kotlinx.android.synthetic.main.fragment_test.view.*

class HomeFragment : BaseFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    //val rootView  = inflater.inflate(R.layout.fragment_test, container, false)
    //val userView = rootView.userView

    //userView.user = AccountManager

    //return rootView

    //return inflater.inflate(R.layout.fragment_request_detail, container, false)
    return inflater.inflate(R.layout.fragment_test, container, false)
  }
}
