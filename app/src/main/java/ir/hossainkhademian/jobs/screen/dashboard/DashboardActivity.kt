package ir.hossainkhademian.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.dialog.WhatsNewDialog
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.Collections.consume
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity() {
  private var currentFragment: Fragment? = null

  private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_about -> consume {
        showAbout()
      }
      R.id.navigation_dashboard -> consume(false) {
        showRequests()
      }
      R.id.navigation_chats -> consume(false) {
        showChats()
      }
      else -> consume {
        Snackbar.make(container, "Not Implemented yet!", Snackbar.LENGTH_LONG).show()
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dashboard)

    initWhatsNew()

    navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    showAbout()
  }

  private fun initWhatsNew() {
    WhatsNewDialog
      .create()
      .presentAutomatically(this)
  }

  private fun setFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
    currentFragment = fragment
  }

  fun showAbout() =
    setFragment(TestFragment())
  // setFragment(AboutFragment())

  fun showChats() =
    launchActivity<ChatListActivity>()

  fun showRequests() =
    launchActivity<RequestListActivity>()
}
