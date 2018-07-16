package ir.hossainkhademian.jobs.screen.request.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.Collections.consume
import ir.hossainkhademian.util.LiveDatas.letObserveOn
import ir.hossainkhademian.util.ViewModels.getViewModel
import kotlinx.android.synthetic.main.activity_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail.view.*
import kotlinx.android.synthetic.main.item_request_broker.view.*

class RequestDetailFragment : BaseFragment() {
  companion object {
    const val ARG_REQUEST_ID = ARG_ID
    const val ARG_REQUEST_TITLE = ARG_TITLE
  }

  private lateinit var viewModel: RequestDetailViewModel
  private lateinit var rootView: View
  private val cancelAction get() = rootView.cancelAction
  private val editAction get() = rootView.editAction
  private val typeView get() = rootView.typeView  as ToggleSwitch
  private val jobView get() = rootView.jobView
  private val skillsView get() = rootView.skillsView
  private val userView get() = rootView.userView
  private val detailView get() = rootView.detailView
  private val brokersView get() = rootView.brokersView
  private val matchesView get() = rootView.matchesView

  val context get() = activity!!
  private val brokerAdapter: BrokerAdapter = BrokerAdapter()
  private val matchesAdapter: MatchAdapter = MatchAdapter()

  var onEditListener: (ID) -> Unit = { }
  var onSendChatListener: (ID) -> Unit = { }
  var onCancelListener: (ID) -> Unit = { }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val requestId = arguments?.getString(ARG_REQUEST_ID) ?: ""
    val title = arguments?.getString(ARG_REQUEST_TITLE) ?: ""

    viewModel = getViewModel { RequestDetailViewModel(app, this, requestId) }

    activity?.toolbar?.title = title
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_request_detail, container, false)
    typeView.isEnabled = false
    editAction.setOnClickListener { viewModel.edit() }
    cancelAction.setOnClickListener { viewModel.cancel() }

    detailView.setOnTouchListener { v, event ->
      v.parent.requestDisallowInterceptTouchEvent(true)
      if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP)
        v.parent.requestDisallowInterceptTouchEvent(false)
      false
    }

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
    typeView.setCheckedPosition(if (request.isWorker) 0 else 1)
    jobView.job = request.job
    userView.user = request.user
    detailView.text = request.detail

    skillsView.setTags(request.skills.map { it.title })
    brokerAdapter.items = request.brokers
    matchesAdapter.items = request.matches
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

  private inner class BrokerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val avatarView get() = view.avatarView
    private val titleView get() = view.titleView

    var item = EmptyUser
      set(value) {
        field = value
        showItem(value)
      }

    init {
      view.setOnClickListener {
        viewModel.sendChat(item.id)
      }
    }

    private fun showItem(item: User) {
      titleView.text = item.title

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
