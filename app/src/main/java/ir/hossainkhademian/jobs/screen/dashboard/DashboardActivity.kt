package ir.hossainkhademian.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.Toast
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.dialog.ExitDialog
import ir.hossainkhademian.jobs.dialog.WhatsNewDialog
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.Collections.consume
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
  private var currentFragment: Fragment? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dashboard)

    setSupportActionBar(toolbar)
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    bottomNavigation.setOnNavigationItemSelectedListener(this)
    navigationView.setNavigationItemSelectedListener(this)

    initWhatsNew()

    showAbout()
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0)
      supportFragmentManager.popBackStack()
    else ExitDialog.show(context,
      onStay = {

      },
      onRate = {
        Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()
        super.onBackPressed()
      },
      onExit = {
        super.onBackPressed()
      })
  }

  override fun onNavigationItemSelected(item: MenuItem) =
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
