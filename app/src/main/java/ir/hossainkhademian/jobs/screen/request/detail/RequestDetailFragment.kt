package ir.hossainkhademian.jobs.screen.request.detail

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.letObserveOn
import ir.hossainkhademian.util.ViewModels.getViewModel
import kotlinx.android.synthetic.main.activity_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail.view.*
import kotlinx.android.synthetic.main.item_request_broker.view.*
import java.text.FieldPosition

class RequestDetailFragment : BaseFragment() {
  companion object {
    const val ARG_REQUEST_ID = "request.id"
    const val ARG_REQUEST_TITLE = "request.title"
  }

  private lateinit var viewModel: RequestDetailViewModel
  private lateinit var rootView: View
  private val cancelAction get() = rootView.cancelAction
  private val editAction get() = rootView.editAction
  private val requestTypeWorkerView get() = rootView.requestTypeWorkerView
  private val requestTypeCompanyView get() = rootView.requestTypeCompanyView
  private val requestView get() = rootView.requestView
  private val skillsView get() = rootView.skillsView
  private val userView get() = rootView.userView
  private val detailView get() = rootView.detailView
  private val brokersView get() = rootView.brokersView
  private val matchesView get() = rootView.matchesView

  val context get() = activity!!
  private val brokerAdapter: BrokerAdapter = BrokerAdapter()
  private val matchesAdapter: MatchAdapter = MatchAdapter()

  var onEditListener: (Request) -> Unit = { }
  var onCancelListener: (Request) -> Unit = { }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val requestId = arguments?.getString(ARG_REQUEST_ID) ?: ""
    val title = arguments?.getString(ARG_REQUEST_TITLE) ?: ""

    viewModel = getViewModel { RequestDetailViewModel(app, requestId) }

    activity?.toolbar?.title = title
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_request_detail, container, false)

    brokersView.let { recyclerView ->
      recyclerView.adapter = brokerAdapter
      recyclerView.itemAnimator = DefaultItemAnimator()
      recyclerView.layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.HORIZONTAL
      }
    }

    matchesView.let { recyclerView ->
      recyclerView.adapter = matchesAdapter
      recyclerView.itemAnimator = DefaultItemAnimator()
      recyclerView.layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
      }
      recyclerView.itemAnimator = DefaultItemAnimator()
    }

    viewModel.request.letObserveOn(this, EmptyRequest, ::setRequest)

    return rootView
  }

  private fun setRequest(request: Request) {
    editAction.setOnClickListener { onEditListener(request) }
    cancelAction.setOnClickListener { viewModel.cancel(onCancelListener) }

    requestTypeWorkerView.isEnabled = request.type == RequestType.WORKER
    requestTypeCompanyView.isEnabled = request.type == RequestType.COMPANY
    requestView.request = request.job
    userView.user = request.user
    detailView.text = request.detail

    brokerAdapter.items = request.brokers
    matchesAdapter.items = request.matches

    skillsView.setTags(request.skills.map { it.title })
  }


  private inner class BrokerAdapter(items: List<User> = emptyList()) : RecyclerView.Adapter<BrokerViewHolder>() {
    var items = items
      set(value) {
        field = value
        notifyDataSetChanged()
      }

    override fun getItemCount() = items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      BrokerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_request_broker, parent, false))


    override fun onBindViewHolder(viewHolder: BrokerViewHolder, position: Int) {
      viewHolder.item = items[position]
    }
  }

  private inner class MatchAdapter(items: List<Match> = emptyList()) : RecyclerView.Adapter<MatchViewHolder>() {
    var items = items
      set(value) {
        field = value
        notifyDataSetChanged()
      }

    override fun getItemCount() = items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      MatchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_request_match, parent, false))


    override fun onBindViewHolder(viewHolder: MatchViewHolder, position: Int) {
      viewHolder.item = items[position]
    }
  }

  private class BrokerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val avatarView = view as CircleImageView

    var item = EmptyUser
      set(value) {
        field = value
        showItem(value)
      }

    private fun showItem(item: User) {
      Picasso.get().load(item.avatarUrl)
        .placeholder(R.drawable.ic_avatar)
        .error(R.drawable.ic_avatar)
        .into(avatarView)
    }
  }

  private class MatchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val titleView = view as TextView

    var item = EmptyMatch
      set(value) {
        field = value
        showItem(value)
      }

    private fun showItem(item: Match) {
      titleView.text = item.toString()
    }
  }
}
