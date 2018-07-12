package ir.hossainco.jobs.screen.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import ir.hossainco.jobs.R
import ir.hossainco.jobs.screen.BaseActivity
import ir.hossainco.jobs.screen.chat.ChatListActivity
import ir.hossainco.jobs.screen.dashboard.about.AboutFragment
import ir.hossainco.jobs.util.context
import ir.hossainco.jobs.util.launchActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity() {
	private var currentFragment: Fragment? = null

	private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.itemId) {
			R.id.navigation_about -> {
				showAbout()
				return@OnNavigationItemSelectedListener true
			}
//			R.id.navigation_dashboard -> {
//				return@OnNavigationItemSelectedListener true
//			}
			R.id.navigation_chats -> {
				showChats()
				return@OnNavigationItemSelectedListener true
			}
			else -> {
				Snackbar.make(container, "Not Implemented yet!", Snackbar.LENGTH_LONG).show()
				return@OnNavigationItemSelectedListener false
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_dashboard)
		navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
		showAbout()

		AlertDialog.Builder(context).setTitle("MOCK !!!")
			.setMessage("In Test mode:\n\n" +
				"currently: because of my limited time and other business except this contest, I just developed Chat part, and database needed for more of the app logic\n\n" +
				"I use Android Architecture components (Room, LiveData) and Android Support Widgets (Toolbar, CoordinatorLayout, ConstraintLayout, RecyclerView, CardView, ...)\n" +
				"and some good libraries (Picasso, CircleImageView, Square Leaks, Masked EditText, BadgeView, ...)\n\n" +
				"currently fake data (Chat & Users) generated at app start and fake user profile pictures load from net\n\n" +
				"there is a lot pending work: Koin (dependencies injection), ViewModel and complete MVVM arch\n" +
				"job lists, ability list, provide users to post and modify Job requests (worker & companies), users to edit their resume, brokers to connect related requests, admins to see and modify data and connect requests to brokers\n" +
				"settings screen, profile screen, ...\n\n" +
				"till then Happy exploring!")
			.setNeutralButton("OK !", { dialog, which -> dialog.cancel() })
			.setCancelable(false)
			.show()


	}

	private fun setFragment(fragment: Fragment) {
		supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
		currentFragment = fragment
	}

	fun showAbout() =
		setFragment(AboutFragment())

	fun showChats() =
		launchActivity<ChatListActivity>()
}
