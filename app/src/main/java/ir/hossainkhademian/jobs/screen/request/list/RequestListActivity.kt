package ir.hossainkhademian.jobs.screen.request.list

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_request_list.*
import kotlinx.android.synthetic.main.fragment_request_list.*
import kotlinx.android.synthetic.main.item_request_list.view.*
import ir.hossainkhademian.util.LiveDatas.observeOn
import ir.hossainkhademian.util.bundle


class RequestListActivity : AppCompatActivity() {
  private val picasso = Picasso.get()!!
  private var twoPane: Boolean = false
  private lateinit var viewModel: RequestListViewModel

  private val adapter = RequestAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_request_list)
    twoPane = request_detail_container != null

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

//    fab.setOnClickListener { view ->
//      Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }

    setupRecyclerView(request_list)

    viewModel = getViewModel { RequestListViewModel() }

    viewModel.requests.observeOn(this) { items ->
      adapter.items = items ?: emptyList()
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

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.layoutManager = LinearLayoutManager(context).apply {
      orientation = LinearLayoutManager.VERTICAL
      reverseLayout = false
      stackFromEnd = false
    }
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    recyclerView.adapter = adapter
  }

  private fun onItemSelected(item: Request) {
    showRequestDetail(item)
  }

  private fun showRequestDetail(item: Request) {
    if (twoPane) {
      val fragment = RequestDetailFragment().apply {
        arguments = bundle(
          RequestDetailFragment.ARG_REQUEST_TITLE to item.title,
          RequestDetailFragment.ARG_REQUEST_ID to item.idStr
        )
      }
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.chat_detail_container, fragment)
        .commit()
    } else {
      launchActivity<RequestDetailActivity>(extras = *arrayOf(
        RequestDetailFragment.ARG_REQUEST_TITLE to item.title,
        RequestDetailFragment.ARG_REQUEST_ID to item.idStr
      ))
    }
  }

  private inner class RequestAdapter(data: List<Request> = emptyList()) : RecyclerView.Adapter<ViewHolder>() {
    var items = data
      set(items) {
        field = items
        notifyDataSetChanged()
      }

    override fun getItemCount() =
      items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      ViewHolder(LayoutInflater.from(context), parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.item = items[position]
    }
  }

  private inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val avatarView = view.avatarView!!
    val titleView = view.titleView!!
    val subtitleView = view.subtitleView!!

    var item: Request = EmptyRequest
      set(item) {
        field = item
        showItem(item)
      }

    constructor(inflater: LayoutInflater, parent: ViewGroup) :
      this(inflater.inflate(R.layout.item_request_list, parent, false))

    init {
      view.setOnClickListener {
        onItemSelected(item)
      }
    }

    private fun showItem(item: Request) {
      val imageUrl = item.avatarUrl
      val color = view.context.resources.getColor(
        if (item.isWorker) R.color.color_request_worker
        else R.color.color_request_company)

      avatarView.borderColor = color
      picasso.load(imageUrl).placeholder(R.drawable.ic_avatar).into(avatarView)

      titleView.text = item.title
      subtitleView.text = item.subtitle
    }
  }
}
