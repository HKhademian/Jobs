package ir.chista.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import ir.chista.jobs.R
import ir.chista.jobs.dialog.ExitDialog
import ir.chista.jobs.dialog.WhatsNewDialog
import ir.chista.jobs.util.BaseActivity
import ir.chista.jobs.screen.chat.list.ChatListActivity
import ir.chista.jobs.screen.dashboard.about.AboutFragment
import ir.chista.jobs.screen.dashboard.home.HomeFragment
import ir.chista.jobs.screen.dashboard.navigation.DashboardNavigationFragment
import ir.chista.jobs.screen.dashboard.navigation.DashboardNavigationListener
import ir.chista.jobs.screen.request.list.RequestListActivity
import ir.chista.util.Collections.consume
import ir.chista.util.context
import ir.chista.util.launchActivity
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

      setFragment<HomeFragment>()
      initWhatsNew()
    }
  }

  override fun onBackPressed() {
    when {
      drawer_layout.isDrawerOpen(Gravity.START) -> drawer_layout.closeDrawer(Gravity.START)

      // supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()

      else -> onNavigationExit()
    }
  }

  override fun onNavigationClose() {
    drawer_layout.closeDrawer(Gravity.START)
  }

  override fun onNavigationExit() {
    ExitDialog.show(context,
      onStay = { },
      onRate = {
        Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()
      },
      onExit = {
        finish()
      })
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    onNavigationClose()

    return when (item.itemId) {
      R.id.navigation_dashboard -> consume {
        setFragment<HomeFragment>()
      }
      R.id.navigation_about -> consume {
        addFragment<AboutFragment>()
      }
      R.id.navigation_chats -> consume(false) {
        launchActivity<ChatListActivity>()
      }
      R.id.navigation_requests -> consume(false) {
        launchActivity<RequestListActivity>()
      }

    //ViewModel implemented
    //R.id.nav_logout -> consume(true) {
    //  onNavigationExit()
    //}
    //R.id.nav_refresh -> consume(true) {
    //  onNavigationExit()
    //}
    //R.id.nav_exit -> consume(true) {
    //  onNavigationExit()
    //}

    //R.id.nav_share -> consume(true) {
    //}
    //R.id.nav_send-> consume(true) {
    //}
    //R.id.nav_developer -> consume(true) {
    //}
    //R.id.nav_fanavard -> consume(true) {
    //}

      else -> consume(false) {
        Snackbar.make(container, "Not Implemented yet!", Snackbar.LENGTH_SHORT).show()
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
      .addToBackStack(tag)
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
     /* supportFragmentManager.findFragmentByTag(tag) as? T
        ?: */ T::class.java.newInstance()!!
    fragment.apply(init)
    return tag to fragment
  }

}
