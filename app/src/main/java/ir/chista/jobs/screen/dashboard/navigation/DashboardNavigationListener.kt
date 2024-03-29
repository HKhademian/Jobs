package ir.chista.jobs.screen.dashboard.navigation

interface DashboardNavigationListener {
  fun onNavigationClose()
  fun onNavigationExit()

  fun onNavigationLock(lock: Boolean)
  fun onNavigationTapDone()
}
