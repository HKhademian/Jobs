package ir.hossainkhademian.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.dialog.ExitDialog
import ir.hossainkhademian.jobs.dialog.WhatsNewDialog
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.dashboard.about.AboutFragment
import ir.hossainkhademian.jobs.screen.dashboard.navigation.DashboardNavigationFragment
import ir.hossainkhademian.jobs.screen.dashboard.navigation.DashboardNavigationListener
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.Collections.consume
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity(),
  BottomNavigationView.OnNavigationItemSelectedListener,
  NavigationView.OnNavigationItemSelectedListener,
  DashboardNavigationListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dashboard)

    setSupportActionBar(toolbar)
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    bottomNavigation.setOnNavigationItemSelectedListener(this)

    if (savedInstanceState == null) {
      val navigation = DashboardNavigationFragment()
      supportFragmentManager.beginTransaction()
        .replace(R.id.navigationFrame, navigation)
        .commit()

      drawer_layout.openDrawer(Gravity.START)

      setFragment<TestFragment>()
      initWhatsNew()
    }
  }

  override fun onBackPressed() {
    when {
      drawer_layout.isDrawerOpen(Gravity.START) -> drawer_layout.closeDrawer(Gravity.START)

      supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()

      else -> onNavigationClose()
    }
  }

  override fun onNavigationClose() {
    ExitDialog.show(context,
      onStay = { },
      onRate = {
        Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()
      },
      onExit = {
        finish()
      })
  }

  override fun onNavigationExit() {
    drawer_layout.closeDrawer(Gravity.START)
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    onNavigationClose()
    return when (item.itemId) {
      R.id.navigation_dashboard -> consume {
        setFragment<TestFragment>()
      }
      R.id.navigation_about -> consume {
        addFragment<AboutFragment>()
      }
      R.id.navigation_requests -> consume(false) {
        launchActivity<ChatListActivity>()
      }
      R.id.navigation_chats -> consume(false) {
        launchActivity<RequestListActivity>()
      }

      R.id.nav_exit -> consume(true) {
        onNavigationExit()
      }
    //R.id.nav_share -> consume(true) {
    //}
    //R.id.nav_send-> consume(true) {
    //}
    //R.id.nav_developer -> consume(true) {
    //}
    //R.id.nav_fanavard -> consume(true) {
    //}

      else -> consume(false) {
        Snackbar.make(container, "Not Implemented yet!", Snackbar.LENGTH_LONG).show()
      }
    }
  }

  private fun initWhatsNew() {
    WhatsNewDialog
      .create()
      .presentAutomatically(this)
  }

  private fun setFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragment, fragment, tag)
      .commit()
  }

  private fun addFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.beginTransaction()
      .add(R.id.fragment, fragment, tag)
      .commit()
  }

  private inline fun <reified T : Fragment> setFragment(init: T.() -> Unit = {}) {
    val (tag, fragment) = getFragment(init)
    setFragment(fragment, tag)
  }

  private inline fun <reified T : Fragment> addFragment(init: T.() -> Unit = {}) {
    val (tag, fragment) = getFragment(init)
    addFragment(fragment, tag)
  }

  private inline fun <reified T : Fragment> getFragment(init: T.() -> Unit = {}): Pair<String, T> {
    val tag = T::class.java.simpleName
    val fragment =
      supportFragmentManager.findFragmentByTag(tag) as? T
        ?: T::class.java.newInstance()!!
    fragment.apply(init)
    return tag to fragment
  }

}
