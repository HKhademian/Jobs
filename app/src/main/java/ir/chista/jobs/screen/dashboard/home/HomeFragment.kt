package ir.chista.jobs.screen.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.chista.jobs.R
import ir.chista.jobs.util.BaseFragment

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
