package ir.chista.jobs.screen.request.list

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.widget.Toast
import com.squareup.picasso.Picasso
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.screen.chat.detail.ChatDetailActivity
import ir.chista.jobs.screen.chat.detail.ChatDetailFragment
import ir.chista.jobs.screen.chat.list.ChatListActivity
import ir.chista.jobs.screen.request.detail.RequestDetailActivity
import ir.chista.jobs.screen.request.detail.RequestDetailFragment
import ir.chista.jobs.screen.request.detail.RequestDetailListener
import ir.chista.jobs.screen.request.edit.RequestEditActivity
import ir.chista.jobs.screen.request.edit.RequestEditFragment
import ir.chista.jobs.screen.request.edit.RequestEditListener
import ir.chista.util.ViewModels.viewModel
import ir.chista.util.context
import ir.chista.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_list_holder.*
import kotlinx.android.synthetic.main.activity_request_list.*
import kotlinx.android.synthetic.main.fragment_request_list.*
import kotlinx.android.synthetic.main.item_request_list.view.*
import ir.chista.util.LiveDatas.observe
import ir.chista.util.activity
import ir.chista.util.bundle
import android.view.Gravity
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import ir.chista.jobs.data.AccountManager


class RequestListActivity : AppCompatActivity(), RequestDetailListener, RequestEditListener {
  private val picasso = Picasso.get()!!
  private var twoPane: Boolean = false
  private var navigationLocked: Boolean = false
  private lateinit var viewModel: RequestListViewModel

  private val adapter = RequestAdapter()
  private var editing = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = viewModel { RequestListViewModel() }
    viewModel.init()

    setContentView(R.layout.activity_request_list_holder)
    twoPane = detailContainer != null

    fab?.let { fab ->
      fab.layoutParams = CoordinatorLayout.LayoutParams(fab.layoutParams as? CoordinatorLayout.LayoutParams).also {
        it.gravity =
          if (twoPane) Gravity.BOTTOM or Gravity.START
          else Gravity.TOP or Gravity.CENTER
      }
    }
    fabDetail?.let { fab ->
      fab.visibility = View.GONE
//      fab.layoutParams = CoordinatorLayout.LayoutParams(fab.layoutParams as? CoordinatorLayout.LayoutParams).also {
//        it.gravity =
//          if (twoPane) Gravity.BOTTOM or Gravity.END
//          else Gravity.TOP or Gravity.END
//      }
    }

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

    fab.setOnClickListener {
      showRequestEdit(emptyID)
    }

    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    recyclerView.adapter = adapter

    swipeRefreshLayout.setOnRefreshListener {
      Toast.makeText(activity, "Refreshing", Toast.LENGTH_SHORT).show()
      viewModel.refresh()
    }

    viewModel.error.observe(this) {
      val ex = it.getContentIfNotHandled() ?: return@observe
      Snackbar.make(recyclerView,
        "Error :\n${ex.message ?: ex.toString()}\n\nif it happens many times, contact support",
        Snackbar.LENGTH_LONG).show()
    }

    viewModel.requests.observe(this) { items ->
      adapter.items = items
    }

    viewModel.isEditable.observe(this) { isEditable ->
      swipeRefreshLayout.isEnabled = isEditable
      fab.isEnabled = isEditable
      fab.visibility = if (isEditable) View.VISIBLE else View.GONE
    }

    viewModel.selectedId.observe(this) { selectedId ->
      adapter.selectedId = selectedId
    }

    viewModel.isRefreshing.observe(this) { isRefreshing ->
      swipeRefreshLayout.isRefreshing = isRefreshing
      Toast.makeText(activity, "isRefreshing:$isRefreshing", Toast.LENGTH_SHORT).show()
    }

    if (savedInstanceState == null)
      mayShowTap()
  }

  private fun mayShowTap() {
    if (AccountManager.Taps.requestListDone)
      return
    val taps = mutableListOf<TapTarget>()

    if (fab != null)
      taps += TapTarget.forView(fab, getString(R.string.tap_requestList_fab_title), getString(R.string.tap_requestList_fab_des)).transparentTarget(true)//.cancelable(false)
    taps += TapTarget.forView(recyclerView, getString(R.string.tap_requestList_title), getString(R.string.tap_requestList_des)).transparentTarget(true)//.cancelable(false)

    navigationLocked = true
    TapTargetSequence(this).targets(taps)
      .listener(object : TapTargetSequence.Listener {
        override fun onSequenceCanceled(lastTarget: TapTarget?) {
          navigationLocked = false
          Snackbar.make(recyclerView, "I'll back again!", Snackbar.LENGTH_SHORT).show()
        }

        override fun onSequenceFinish() {
          navigationLocked = false
          AccountManager.Taps.requestListDone = true
        }

        override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) = Unit // hmmm!
      })
      .continueOnCancel(true)
      .start()

  }

  private fun showRequestDetail(request: Request) {
    if (editing) {
      Snackbar.make(recyclerView, "Complete your editing please.", Snackbar.LENGTH_SHORT).show()
      return
    }
    viewModel.postSelectedId(request.id)

    if (!twoPane) {
      launchActivity<RequestDetailActivity>(extras = *arrayOf(
        RequestDetailFragment.ARG_REQUEST_ID to request.idStr
      ))
    } else {
      val tag = RequestDetailFragment::class.java.simpleName
      val fragment =
        (supportFragmentManager.findFragmentByTag(tag) as? RequestDetailFragment)?.also { it.setRequestId(request.id) }
          ?: RequestDetailFragment().also { it.arguments = bundle(RequestDetailFragment.ARG_REQUEST_ID to request.idStr) }

      supportFragmentManager
        .beginTransaction()
        .replace(R.id.detailContainer, fragment, tag)
        .commit()
    }
  }

  private fun showRequestEdit(requestId: ID) {
    if (editing) {
      Snackbar.make(recyclerView, "Complete your editing please.", Snackbar.LENGTH_SHORT).show()
      return
    }
    viewModel.postSelectedId(requestId)

    if (!twoPane) {
      launchActivity<RequestEditActivity>(extras = *arrayOf(
        RequestDetailFragment.ARG_REQUEST_ID to requestId
      ))
    } else {
      val tag = RequestEditFragment::class.java.simpleName

      // rare happens if cache available
      val fragment =
        (supportFragmentManager.findFragmentByTag(tag) as? RequestEditFragment)?.also { it.setRequestId(requestId) }
          ?: RequestEditFragment().also { it.arguments = bundle(RequestEditFragment.ARG_REQUEST_ID to requestId) }

      supportFragmentManager
        .beginTransaction()
        .add(R.id.detailContainer, fragment, tag)
        .addToBackStack("$tag:$requestId")
        .commit()

      editing = true
    }
  }

  override fun onBackPressed() {
    if (navigationLocked)
      return
    editing = false
    super.onBackPressed()
  }

  private fun onItemSelected(request: Request) {
    showRequestDetail(request)
  }

  //override fun onCreateOptionsMenu(menu: Menu?): Boolean {
  //  menuInflater.inflate(R.menu.request_list, menu)
  //  return true
  //}

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        NavUtils.navigateUpFromSameTask(this)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  /** this is never called in single panel mode */
  override fun onRequestDetailEdit(requestId: ID) {
    showRequestEdit(requestId)
  }

  /** this is never called in single panel mode */
  override fun onRequestDetailChat(requestId: ID, userId: ID) {
    if (userId.isNotEmpty)
      if (twoPane)
        launchActivity<ChatListActivity>(extras = *arrayOf(
          ChatDetailFragment.ARG_CONTACT_ID to userId
        ))
      else // i think it's impossible
        launchActivity<ChatDetailActivity>(extras = *arrayOf(
          ChatDetailFragment.ARG_CONTACT_ID to userId
        ))
  }

  /** this is never called in single panel mode */
  override fun onRequestDetailCloseDone(requestId: ID) {
    Snackbar.make(toolbar, "Not Implemented!", Snackbar.LENGTH_SHORT).show()

// editing = false
    // supportFragmentManager.popBackStack()
  }

  /** this is never called in single panel mode */
  override fun onRequestEditDone(requestId: ID) {
    editing = false
    viewModel.postSelectedId(requestId)
    supportFragmentManager.popBackStack()
  }

  /** this is never called in single panel mode */
  override fun onRequestEditCancel(requestId: ID) {
    editing = false
    supportFragmentManager.popBackStack()
  }


  private inner class RequestAdapter(items: List<LocalRequest> = emptyList(), selectedId: ID = emptyID) : RecyclerView.Adapter<ViewHolder>() {
    var items = items
      set(items) {
        field = items
        notifyDataSetChanged()
      }

    var selectedId = selectedId
      set(selectedId) {
        field = selectedId
        notifyDataSetChanged()
      }

    override fun getItemCount() =
      items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      ViewHolder(LayoutInflater.from(context), parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item = items[position]
      holder.item = item
      holder.selected = item.id == selectedId
    }
  }

  private inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val avatarView = view.avatarView!!
    val titleView = view.titleView!!
    val subtitleView = view.subtitleView!!

    var item: LocalRequest = EmptyRequest
      set(item) {
        field = item
        showItem(item, selected)
      }
    var selected: Boolean = false
      set(selected) {
        field = selected
        showItem(item, selected)
      }

    constructor(inflater: LayoutInflater, parent: ViewGroup) :
      this(inflater.inflate(R.layout.item_request_list, parent, false))

    init {
      view.setOnClickListener {
        onItemSelected(item)
      }
    }

    private fun showItem(item: LocalRequest, selected: Boolean) {
      val imageUrl = item.avatarUrl

      view.setBackgroundColor(if (twoPane && selected) context.resources.getColor(R.color.colorSelector) else 0)

      avatarView.borderColor = view.context.resources.getColor(
        if (item.isWorker) R.color.color_request_worker
        else R.color.color_request_company)
      picasso.load(imageUrl).placeholder(R.drawable.ic_avatar).into(avatarView)

      titleView.text = item.title
      subtitleView.text = item.subtitle
    }
  }
}
