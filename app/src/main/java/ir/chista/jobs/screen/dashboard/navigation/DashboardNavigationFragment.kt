package ir.chista.jobs.screen.dashboard.navigation

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import com.squareup.picasso.Picasso
import ir.chista.jobs.R
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.data.model.UserRole
import ir.chista.jobs.data.model.avatarUrl
import ir.chista.jobs.util.BaseFragment
import ir.chista.util.Collections.consume
import ir.chista.util.LiveDatas.observe
import ir.chista.util.ViewModels.viewModel
import kotlinx.android.synthetic.main.fragment_navigation.view.*
import kotlinx.android.synthetic.main.nav_header_dashboard.view.*
import java.lang.ref.WeakReference

class DashboardNavigationFragment : BaseFragment() {
  private lateinit var rootView: View
  private val navigationView get() = rootView.navigationView
  private val navigationHeader get() = navigationView.getHeaderView(0)
  private val logoutAction get() = navigationHeader.logoutAction
  private val editAction get() = navigationHeader.editAction
  private val avatarView get() = navigationHeader.avatarView
  private val titleView get() = navigationHeader.titleView
  private val subtitleView get() = navigationHeader.subtitleView

  private lateinit var viewModel: DashboardNavigationViewModel
  private var navigationListener: WeakReference<NavigationView.OnNavigationItemSelectedListener>? = null

  @Suppress("WhenWithOnlyElse")
  private val navListener = NavigationView.OnNavigationItemSelectedListener {
    val listener = navigationListener?.get()
    when (it.itemId) {
      R.id.nav_refresh -> consume { viewModel.refresh() }

      R.id.nav_logout -> consume { viewModel.logout() }

      R.id.nav_exit -> consume { viewModel.exit() }

      else -> listener?.onNavigationItemSelected(it) ?: false
    }
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    viewModel = viewModel { DashboardNavigationViewModel() }
    viewModel.listener = (context as? DashboardNavigationListener)?.let { WeakReference(it) }
    navigationListener = (context as? NavigationView.OnNavigationItemSelectedListener)?.let { WeakReference(it) }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (savedInstanceState == null) {
      viewModel.init()
    }
    if (this::rootView.isInitialized)
      return rootView
    rootView = inflater.inflate(R.layout.fragment_navigation, container, false)

    viewModel.error.observe(this) {
      val ex = it.getContentIfNotHandled() ?: return@observe
      Snackbar.make(rootView,
        "Error in dashboard navigator :\n${ex.message ?: ex.toString()}\n\nif it happens many times, contact support",
        Snackbar.LENGTH_SHORT).show()
    }

    viewModel.activity.observe(this) {
      val task = it.getContentIfNotHandled() ?: return@observe
      activity?.let { activity -> task.invoke(activity) }
    }

    viewModel.user.observe(this) {
      Picasso.get().load(it.avatarUrl)
        .placeholder(R.drawable.ic_avatar)
        .error(R.drawable.ic_avatar)
        .into(avatarView)
    }

    viewModel.title.observe(this) {
      titleView.text = it
    }

    viewModel.subtitle.observe(this) {
      subtitleView.text = it
    }

    viewModel.user.observe(this) {
      avatarView.circleBackgroundColor = activity?.resources?.getColor(when (it.role) {
        UserRole.User -> R.color.color_role_user
        UserRole.Broker -> R.color.color_role_broker
        UserRole.Admin -> R.color.color_role_admin
      }) ?: avatarView.circleBackgroundColor
    }

    logoutAction.setOnClickListener { viewModel.logout() }
    editAction.setOnClickListener { viewModel.edit() }

    navigationView.setNavigationItemSelectedListener(navListener)

    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (!AccountManager.Taps.navigationDone)
      activity?.let { showTapTarget(it) }
    else
      viewModel.listener?.get()?.onNavigationTapDone()
  }


  private fun showTapTarget(activity: Activity) = with(activity) {
    viewModel.listener?.get()?.onNavigationLock(true)
    TapTargetSequence(this).targets(
      TapTarget.forView(avatarView, getString(R.string.tap_avatar_title), getString(R.string.tap_avatar_des)).transparentTarget(true),//.cancelable(false),
      TapTarget.forView(titleView, getString(R.string.tap_title_title), getString(R.string.tap_title_des)).transparentTarget(true),//.cancelable(false),
      TapTarget.forView(subtitleView, getString(R.string.tap_subtitle_title), getString(R.string.tap_subtitle_des)).transparentTarget(true),//.cancelable(false),
      TapTarget.forView(editAction, getString(R.string.tap_edit_title), getString(R.string.tap_edit_des)).transparentTarget(true),//.cancelable(false),
      TapTarget.forView(logoutAction, getString(R.string.tap_logout_title), getString(R.string.tap_logout_des)).transparentTarget(true)//.cancelable(false)
    )
      .listener(object : TapTargetSequence.Listener {
        override fun onSequenceCanceled(lastTarget: TapTarget?) {
          viewModel.listener?.get()?.onNavigationLock(false)
          Snackbar.make(avatarView, "I'll back again! one day, no ..., one dark Night", Snackbar.LENGTH_SHORT).show()
        }

        override fun onSequenceFinish() {
          AccountManager.Taps.navigationDone = true
          viewModel.listener?.get()?.onNavigationLock(false)
          viewModel.listener?.get()?.onNavigationTapDone()
        }

        override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) = Unit // hmmm!
      })
      .continueOnCancel(true)
      .start()
  }

}
