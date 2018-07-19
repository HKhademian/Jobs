package ir.chista.jobs.screen.request.detail

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import com.squareup.picasso.Picasso
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseFragment
import ir.chista.util.LiveDatas.observe
import ir.chista.util.ViewModels.viewModel

import kotlinx.android.synthetic.main.activity_request_list_holder.toolbar as listToolbar
import kotlinx.android.synthetic.main.activity_request_detail.toolbar as toolbar
import kotlinx.android.synthetic.main.fragment_request_detail.view.*
import kotlinx.android.synthetic.main.item_request_broker.view.*

class RequestDetailFragment : BaseFragment() {
  companion object {
    const val ARG_REQUEST_ID = ARG_ID
  }

  private lateinit var viewModel: RequestDetailViewModel
  private lateinit var rootView: View
  private val cancelAction get() = rootView.cancelAction
  private val editAction get() = rootView.editAction
  private val typeView get() = rootView.typeView as ToggleSwitch
  private val jobView get() = rootView.jobView
  private val skillsView get() = rootView.skillsView
  private val skillsEmptyHintView get() = rootView.skillsEmptyHintView
  private val userView get() = rootView.userView
  private val detailView get() = rootView.detailView
  private val detailsEmptyHintView get() = rootView.detailsEmptyHintView
  private val brokersCard get() = rootView.brokersCard
  private val brokersView get() = rootView.brokersView
  private val brokersRemoveHintView get() = rootView.brokersRemoveHintView
  private val brokersEmptyHintView get() = rootView.brokersEmptyHintView
  private val matchesView get() = rootView.matchesView
  private val matchesEmptyHintView get() = rootView.matchesEmptyHintView

  val context get() = activity!!
  private val brokerAdapter: BrokerAdapter = BrokerAdapter()
  private val matchesAdapter: MatchAdapter = MatchAdapter()

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    viewModel = viewModel { RequestDetailViewModel() }
    viewModel.listener = context as? RequestDetailListener
  }

  fun setRequestId(requestId: ID) {
    viewModel.requestId = requestId
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (savedInstanceState == null) {
      viewModel.init(arguments?.getString(ARG_REQUEST_ID) ?: emptyID)
    }

    if (this::rootView.isInitialized)
      return rootView
    rootView = inflater.inflate(R.layout.fragment_request_detail, container, false)

    brokersView.adapter = brokerAdapter
    matchesView.adapter = matchesAdapter

    typeView.isEnabled = false
    editAction.setOnClickListener { viewModel.edit() }
    cancelAction.setOnClickListener { viewModel.cancel() }
    brokersCard.onActionClickListener = View.OnClickListener { if (viewModel.isBrokerEditable.value == true) viewModel.addBroker() }

    viewModel.activity.observe(this) {
      val task = it.getContentIfNotHandled() ?: return@observe
      task.invoke(context as Activity)
    }

    viewModel.error.observe(this) {
      val ex = it.getContentIfNotHandled() ?: return@observe
      Snackbar.make(rootView, "Error occurs in Request Edit Page:\n${ex.message ?: ex.toString()}\n\nif this happens many times please contact support team.", Snackbar.LENGTH_SHORT).show()
    }

    viewModel.isEditable.observe(this) { isEditable ->
      editAction.isEnabled = isEditable
      activity?.findViewById<FloatingActionButton>(R.id.fabDetail)?.let { fabDetail ->
        fabDetail.isEnabled = isEditable
        fabDetail.visibility = if (isEditable) View.VISIBLE else View.GONE
      }
    }

    viewModel.isBrokerEditable.observe(this) { isBrokerEditable ->
      brokersCard.setActionId(if (isBrokerEditable) R.string.action_add else 0)
      brokersRemoveHintView?.visibility = if (isBrokerEditable) View.VISIBLE else View.GONE
    }

    viewModel.request.observe(this) { setRequest(it) }
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activity?.findViewById<FloatingActionButton>(R.id.fabDetail)?.let { fabDetail ->
      fabDetail.isEnabled = true
      fabDetail.visibility = View.VISIBLE
      fabDetail.setImageResource(R.drawable.ic_action_edit)
      fabDetail.setOnClickListener {
        viewModel.edit()
      }
    }
  }

  private fun setRequest(request: LocalRequest) {
    val job = request.job
    val user = request.user
    val detail = request.detail
    val skills = request.skills
    val brokers = request.brokers
    val matches = request.matches

    typeView.setCheckedPosition(if (request.isWorker) 0 else 1)
    jobView.job = job
    userView.user = user
    detailView.text = detail
    detailsEmptyHintView.visibility = if (detail.isEmpty()) View.VISIBLE else View.GONE

    skillsView.setTags(skills.map { it.title })
    skillsEmptyHintView.visibility = if (skills.isEmpty()) View.VISIBLE else View.GONE

    brokerAdapter.items = brokers
    brokersEmptyHintView.visibility = if (brokers.isEmpty()) View.VISIBLE else View.GONE

    matchesAdapter.items = matches
    matchesEmptyHintView.visibility = if (matches.isEmpty()) View.VISIBLE else View.GONE
  }


  private inner class BrokerAdapter(items: List<LocalUser> = emptyList()) : RecyclerView.Adapter<BrokerViewHolder>() {
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

  private inner class MatchAdapter(items: List<LocalMatch> = emptyList()) : RecyclerView.Adapter<MatchViewHolder>() {
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

    var item: LocalUser = EmptyUser
      set(value) {
        field = value
        showItem(value)
      }

    init {
      view.setOnClickListener {
        viewModel.sendChat(item.id)
      }
      view.setOnLongClickListener {
        if (viewModel.isBrokerEditable.value == true) {
          viewModel.removeBroker(item.id)
          true
        } else false
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

  private class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val titleView = view as TextView

    var item: LocalMatch = EmptyMatch
      set(value) {
        field = value
        showItem(value)
      }

    private fun showItem(item: Match) {
      titleView.text = item.toString()
    }
  }
}
