package ir.hossainkhademian.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.screen.BaseActivity
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.dashboard.about.AboutFragment
import ir.hossainkhademian.jobs.screen.request.list.RequestListActivity
import ir.hossainkhademian.util.Collections.consume
import ir.hossainkhademian.util.context
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
    navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    showAbout()

//    AlertDialog.Builder(context).setTitle("MOCK !!!")
//      .setMessage("In Test mode:\n\n" +
//        "currently: because of my limited time and other business except this contest, I just developed Chat part, and database needed for more of the app logic\n\n" +
//        "I use Android Architecture components (Room, LiveData) and Android Support Widgets (Toolbar, CoordinatorLayout, ConstraintLayout, RecyclerView, CardView, ...)\n" +
//        "and some good libraries (Picasso, CircleImageView, Square Leaks, Masked EditText, BadgeView, ...)\n\n" +
//        "currently fake data (Chat & Users) generated at app start and fake user profile pictures list from net\n\n" +
//        "there is a lot pending work: Koin (dependencies injection), ViewModel and complete MVVM arch\n" +
//        "job lists, ability list, provide usersList to post and modify Job requestsList (worker & companies), usersList to edit their resume, brokers to connect related requestsList, admins to see and modify data and connect requestsList to brokers\n" +
//        "settings screen, profile screen, ...\n\n" +
//        "till then Happy exploring!")
//      .setNeutralButton("OK !", { dialog, which -> dialog.cancel() })
//      .setCancelable(false)
//      .show()
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
