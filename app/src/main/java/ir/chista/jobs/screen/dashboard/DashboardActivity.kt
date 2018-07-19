package ir.chista.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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
import ir.chista.jobs.screen.dashboard.settings.SettingsFragment
import ir.chista.jobs.screen.request.list.RequestListActivity
import ir.chista.util.Collections.consume
import ir.chista.util.context
import ir.chista.util.launchActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.support.v4.view.ViewPager
import com.getkeepsafe.taptargetview.TapTargetView
import android.graphics.drawable.Drawable
import android.graphics.Typeface
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import ir.chista.jobs.data.AccountManager


class DashboardActivity : BaseActivity(),
  BottomNavigationView.OnNavigationItemSelectedListener,
  NavigationView.OnNavigationItemSelectedListener,
  DashboardNavigationListener {

  private var navigationLocked: Boolean = false

  private lateinit var viewPagerAdapter: ViewPagerAdapter
  private val viewPagerListener = object : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
    override fun onPageScrollStateChanged(position: Int) = Unit

    override fun onPageSelected(position: Int) {
      val selectedItemId = when (position) {
        0 -> R.id.navigation_home
        1 -> R.id.navigation_settings
        2 -> R.id.navigation_about
        else -> null
      }
      if (selectedItemId != null) {
        // navigationView.setCheckedItem(selectedItemId)
        if (bottomNavigation.selectedItemId != selectedItemId)
          bottomNavigation.selectedItemId = selectedItemId
      }
    }
  }

  private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
    return@OnNavigationItemSelectedListener when (it.itemId) {
      R.id.navigation_home -> consume(true) {
        viewPager.currentItem = 0
      }
      R.id.navigation_settings -> consume(true) {
        viewPager.currentItem = 1
      }
      R.id.navigation_about -> consume(true) {
        viewPager.currentItem = 2
      }
      R.id.navigation_chats -> consume(false) {
        launchActivity<ChatListActivity>()
      }
      R.id.navigation_requests -> consume(false) {
        launchActivity<RequestListActivity>()
      }
      else -> false
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dashboard)

    setSupportActionBar(toolbar)
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

    viewPager.adapter = viewPagerAdapter
    viewPager.addOnPageChangeListener(viewPagerListener)
    bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener)

    if (savedInstanceState == null) {
      val navigation = DashboardNavigationFragment()
      supportFragmentManager.beginTransaction()
        .replace(R.id.navigationFrame, navigation)
        .commit()

      drawer_layout.openDrawer(Gravity.START)

      bottomNavigation.selectedItemId = R.id.navigation_home
      initWhatsNew()
    }
  }

  override fun onBackPressed() {
    if (navigationLocked) return
    when {
      drawer_layout.isDrawerOpen(Gravity.START) -> drawer_layout.closeDrawer(Gravity.START)

      supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStackImmediate()

      viewPager.currentItem != 0 -> viewPager.currentItem = 0

      else -> onNavigationExit()
    }
  }

  override fun onNavigationTapDone() {
    if (AccountManager.Taps.dashboardDone)
      return
    drawer_layout.closeDrawer(Gravity.START)
    navigationLocked = true
    TapTargetSequence(this).targets(
      TapTarget.forToolbarNavigationIcon(toolbar, getString(R.string.tap_dashboard_drawer_title), getString(R.string.tap_dashboard_drawer_des)).transparentTarget(true).cancelable(false),
      TapTarget.forView(bottomNavigation, getString(R.string.tap_dashboard_bottom_title), getString(R.string.tap_dashboard_bottom_des)).transparentTarget(true).cancelable(false)
    )
      .listener(object : TapTargetSequence.Listener {
        override fun onSequenceCanceled(lastTarget: TapTarget?) {
          navigationLocked=false
          Snackbar.make(bottomNavigation, "I'll back again!", Snackbar.LENGTH_SHORT).show()
        }

        override fun onSequenceFinish() {
          navigationLocked=false
          AccountManager.Taps.dashboardDone = true
        }

        override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) = Unit // hmmm!
      })
      .continueOnCancel(true)
      .start()
  }

  override fun onNavigationLock(lock: Boolean) {
    navigationLocked = lock
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

    @Suppress("WhenWithOnlyElse")
    return when (item.itemId) {

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

  private inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount() = 3

    override fun getItem(position: Int) = when (position) {
      0 -> HomeFragment()
      1 -> SettingsFragment()
      2 -> AboutFragment()
      else -> null
    }

    override fun getPageTitle(position: Int) = context.resources.getString(when (position) {
      0 -> R.string.title_screen_home
      1 -> R.string.title_settings
      2 -> R.string.title_screen_about
      else -> R.string.app_name
    })
  }
}
