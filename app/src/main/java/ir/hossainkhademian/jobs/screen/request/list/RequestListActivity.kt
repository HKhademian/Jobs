package ir.hossainkhademian.jobs.screen.request.list

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.request.detail.RequestDetailActivity
import ir.hossainkhademian.jobs.screen.request.detail.RequestDetailFragment
import ir.hossainkhademian.util.ViewModels.getViewModel
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_request_list_holder.*
import kotlinx.android.synthetic.main.activity_request_list.*
import kotlinx.android.synthetic.main.fragment_request_list.*
import kotlinx.android.synthetic.main.item_request_list.view.*
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.bundle


class RequestListActivity : AppCompatActivity() {
  private val picasso = Picasso.get()!!
  private var twoPane: Boolean = false
  private lateinit var viewModel: RequestListViewModel

  private val adapter = RequestAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_request_list_holder)
    twoPane = detailContainer != null

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

//    fab.setOnClickListener { view ->
//      Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }

    setupRecyclerView()

    viewModel = getViewModel { RequestListViewModel() }

    viewModel.error.observe(this) { ex ->
      Snackbar.make(recyclerView, "Error :\n${ex.message ?: ex.toString()}\n\nif it happens many times, contact support", Snackbar.LENGTH_LONG).show()
    }

    viewModel.requests.observe(this, emptyList()) { items ->
      adapter.items = items
    }

    viewModel.isRefreshing.observe(this, false) { isRefreshing ->
      swipeRefreshLayout.isRefreshing = isRefreshing
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        NavUtils.navigateUpFromSameTask(this)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  private fun setupRecyclerView() {
    recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    recyclerView.adapter = adapter

    swipeRefreshLayout.setOnRefreshListener {
      viewModel.refresh()
    }
  }

  private fun onItemSelected(item: LocalRequest) {
    showRequestDetail(item)
  }

  private fun showRequestDetail(item: LocalRequest) {
    if (twoPane) {
      val fragment = RequestDetailFragment().apply {
        arguments = bundle(
          RequestDetailFragment.ARG_REQUEST_TITLE to item.title,
          RequestDetailFragment.ARG_REQUEST_ID to item.idStr
        )
      }
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.detailContainer, fragment)
        .commit()
    } else {
      launchActivity<RequestDetailActivity>(extras = *arrayOf(
        RequestDetailFragment.ARG_REQUEST_TITLE to item.title,
        RequestDetailFragment.ARG_REQUEST_ID to item.idStr
      ))
    }
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

      view.setBackgroundColor(if (twoPane && selected) Color.parseColor("#3666") else 0)

      avatarView.borderColor = view.context.resources.getColor(
        if (item.isWorker) R.color.color_request_worker
        else R.color.color_request_company)
      picasso.load(imageUrl).placeholder(R.drawable.ic_avatar).into(avatarView)

      titleView.text = item.title
      subtitleView.text = item.subtitle
    }
  }
}
