package ir.hossainkhademian.jobs.screen.request.list

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.widget.Toast
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailFragment
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.jobs.screen.request.detail.RequestDetailActivity
import ir.hossainkhademian.jobs.screen.request.detail.RequestDetailFragment
import ir.hossainkhademian.jobs.screen.request.detail.RequestDetailListener
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditFragment
import ir.hossainkhademian.jobs.screen.request.edit.RequestEditListener
import ir.hossainkhademian.util.ViewModels.getViewModel
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_list_holder.*
import kotlinx.android.synthetic.main.activity_request_list.*
import kotlinx.android.synthetic.main.fragment_request_list.*
import kotlinx.android.synthetic.main.item_request_list.view.*
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.activity
import ir.hossainkhademian.util.bundle


class RequestListActivity : AppCompatActivity(), RequestDetailListener, RequestEditListener {
  private val picasso = Picasso.get()!!
  private var twoPane: Boolean = false
  private lateinit var viewModel: RequestListViewModel

  private val adapter = RequestAdapter()
  private var editing = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = getViewModel { RequestListViewModel() }
    viewModel.init()

    setContentView(R.layout.activity_request_list_holder)
    twoPane = detailContainer != null

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
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

    viewModel.requests.observe(this, emptyList()) { items ->
      adapter.items = items
    }

    viewModel.selectedId.observe(this, emptyID) { selectedId ->
      adapter.selectedId = selectedId
    }

    viewModel.isRefreshing.observe(this, false) { isRefreshing ->
      swipeRefreshLayout.isRefreshing = isRefreshing
      Toast.makeText(activity, "isRefreshing:$isRefreshing", Toast.LENGTH_SHORT).show()
    }
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

  override fun onBackPressed() {
    editing = false
    super.onBackPressed()
  }

  private fun onItemSelected(request: Request) {
    showRequestDetail(request)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.request_list, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        NavUtils.navigateUpFromSameTask(this)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  /** this is never called in single panel mode */
  override fun onRequestDetailEdit(request: Request) {
    val tag = RequestEditFragment::class.java.simpleName

    // rare happens if cache available
    val fragment =
      (supportFragmentManager.findFragmentByTag(tag) as? RequestEditFragment)?.also { it.setRequestId(request.id) }
        ?: RequestEditFragment().also { it.arguments = bundle(RequestEditFragment.ARG_REQUEST_ID to request.idStr) }

    supportFragmentManager
      .beginTransaction()
      .add(R.id.detailContainer, fragment, tag)
      .addToBackStack("$tag:${request.id}")
      .commit()

    editing = true
  }

  /** this is never called in single panel mode */
  override fun onRequestDetailChat(request: Request, user: User) {
    if (user.isNotEmpty)
      launchActivity<ChatListActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_CONTACT_ID to user.id
      ))
  }

  /** this is never called in single panel mode */
  override fun onRequestDetailCloseDone(request: Request) {
    //finish()
//    navigateUpTo(Intent(context, RequestListActivity::class.java))
  }

  /** this is never called in single panel mode */
  override fun onRequestEditDone(request: Request, type: RequestType, job: Job, skills: Collection<Skill>, detail: String) {
    editing = false
    supportFragmentManager.popBackStack()
  }

  /** this is never called in single panel mode */
  override fun onRequestEditCancel(request: Request) {
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
