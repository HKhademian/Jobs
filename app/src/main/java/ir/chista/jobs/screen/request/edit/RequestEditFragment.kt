package ir.chista.jobs.screen.request.edit

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.dialog.WaitDialog
import ir.chista.jobs.util.BaseFragment
import ir.chista.util.LiveDatas.observe
import ir.chista.util.TextWatchers
import ir.chista.util.Texts.hideKeyboard
import ir.chista.util.ViewModels.viewModel
import kotlinx.android.synthetic.main.fragment_request_edit.view.*

class RequestEditFragment : BaseFragment() {
  companion object {
    const val ARG_REQUEST_ID = ARG_ID
  }

  private lateinit var viewModel: RequestEditViewModel
  private lateinit var rootView: View
  private val cancelAction get() = rootView.cancelAction
  private val submitAction get() = rootView.submitAction
  private val typeCard get() = rootView.typeCard
  private val typeView get() = rootView.typeView as ToggleSwitch
  private val jobCard get() = rootView.jobCard
  private val jobSelectHintView get() = rootView.jobSelectHintView
  private val jobView get() = rootView.jobView
  private val skillsCard get() = rootView.skillsCard
  private val skillsRemoveHintView get() = rootView.skillsRemoveHintView
  private val skillsView get() = rootView.skillsView
  private val detailCard get() = rootView.detailCard
  private val detailView get() = rootView.detailView

  val clearSkillListener = View.OnClickListener { viewModel.clearSkills() }
  val clearDetailListener = View.OnClickListener { viewModel.clearDetail() }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    viewModel = viewModel { RequestEditViewModel() }
    viewModel.listener = context as? RequestEditListener
  }

  fun setRequestId(id: ID) {
    viewModel.requestId = id
  }

  //@SuppressLint("ClickableViewAccessibility")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (savedInstanceState == null) {
      viewModel.init(arguments?.getString(ARG_REQUEST_ID) ?: emptyID)
    }

    if (this::rootView.isInitialized)
      return rootView

    rootView = inflater.inflate(R.layout.fragment_request_edit, container, false)

    viewModel.activity.observe(this) {
      val task = it.getContentIfNotHandled() ?: return@observe
      task.invoke(context as Activity)
    }

    viewModel.error.observe(this) {
      val ex = it.getContentIfNotHandled() ?: return@observe
      Snackbar.make(rootView, "Error occurs in Request Edit Page:\n${ex.message ?: ex.toString()}\n\nif this happens many times please contact support team.", Snackbar.LENGTH_SHORT).show()
    }

    viewModel.request.observe(this, EmptyRequest) {
      typeView.isEnabled = it.isEmpty // create mode
    }

    viewModel.type.observe(this, RequestType.WORKER) {
      typeView.setCheckedPosition(if (it.isWorker) 0 else 1)
    }

    viewModel.job.observe(this, EmptyJob) {
      jobView.job = it
      jobView.visibility = if (it.isNotEmpty) View.VISIBLE else View.GONE
      jobSelectHintView.visibility = if (it.isEmpty) View.VISIBLE else View.GONE
    }

    viewModel.skills.observe(this, emptySet()) {
      skillsView.setTags(it.map { it.title })
      skillsCard.onAction2ClickListener = if (it.isEmpty()) null else clearSkillListener
      skillsRemoveHintView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
    }

    viewModel.detail.observe(this, "") {
      if (detailView.text.toString() != it) {
        detailView.setText(it)
        if (it.isNotEmpty())
          detailView.setSelection(it.length - 1)
        detailView.clearFocus()
        activity?.hideKeyboard()
      }
      detailCard.onActionClickListener = if (it.isEmpty()) null else clearDetailListener
    }

    viewModel.showWaiting.observe(this) { it ->
      val showWaiting = it.getContentIfNotHandled() ?: return@observe
      activity?.let { activity ->
        if (showWaiting) WaitDialog.show(activity)
        else WaitDialog.dismiss(activity)
      }
    }

    viewModel.isSavable.observe(this, false) { isSavable ->
      activity?.findViewById<FloatingActionButton>(R.id.fabDetail) ?.let { fabDetail ->
        fabDetail.isEnabled = isSavable
        fabDetail.visibility = if (isSavable) View.VISIBLE else View.GONE
      }
      submitAction.isEnabled = isSavable
    }

    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.findViewById<FloatingActionButton>(R.id.fabDetail) ?.let { fabDetail ->
      fabDetail.visibility = View.VISIBLE
      fabDetail.setImageResource(R.drawable.ic_action_save)
      fabDetail.setOnClickListener { viewModel.submit() }
    }

    submitAction.setOnClickListener { viewModel.submit() }
    cancelAction.setOnClickListener { viewModel.cancel() }

    jobCard.onActionClickListener = View.OnClickListener { viewModel.selectJob() }

    skillsCard.onAction1ClickListener = View.OnClickListener { viewModel.addSkill() }
    skillsCard.onAction2ClickListener = clearSkillListener
    skillsView.setOnTagClickListener { viewModel.removeSkill(it) }

    detailCard.onActionClickListener = clearDetailListener
    detailView.addTextChangedListener(TextWatchers.TextWatcher {
      viewModel.onDetailChanged(detailView.text.toString())
    })
  }
}
