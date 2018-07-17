package ir.hossainkhademian.jobs.screen.request.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.TextWatchers
import ir.hossainkhademian.util.Texts.hideKeyboard
import ir.hossainkhademian.util.ViewModels.getViewModel
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
  private val jobView get() = rootView.jobView
  private val skillsCard get() = rootView.skillsCard
  private val skillsRemoveHintView get() = rootView.skillsRemoveHintView
  private val skillsView get() = rootView.skillsView
  private val detailCard get() = rootView.detailCard
  private val detailView get() = rootView.detailView

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    viewModel = getViewModel { RequestEditViewModel() }
    viewModel.init()
    viewModel.requestId = arguments?.getString(ARG_REQUEST_ID) ?: emptyID
    viewModel.listener = context as? RequestEditListener
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_request_edit, container, false)

    submitAction.setOnClickListener { viewModel.submit() }
    cancelAction.setOnClickListener { viewModel.cancel() }

    jobCard.onActionClickListener = View.OnClickListener { viewModel.selectJob() }

    val clearSkillAction = View.OnClickListener { viewModel.clearSkills() }
    skillsCard.onAction1ClickListener = View.OnClickListener { viewModel.addSkill() }
    skillsCard.onAction2ClickListener = clearSkillAction
    skillsView.setOnTagClickListener { viewModel.removeSkill(it) }

    val clearDetailAction = View.OnClickListener { viewModel.clearDetail() }
    detailCard.onActionClickListener = clearDetailAction
//    detailView.setOnTouchListener { v, event ->
//      v.parent.requestDisallowInterceptTouchEvent(true)
//      if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP)
//        v.parent.requestDisallowInterceptTouchEvent(false)
//      false
//    }
    detailView.addTextChangedListener(TextWatchers.TextWatcher {
      viewModel.onDetailChanged(detailView.text.toString())
    })


    viewModel.activity.observe(this) {
      val task = it.getContentIfNotHandled() ?: return@observe
      task.invoke(context as Activity)
    }

    viewModel.request.observe(this, EmptyRequest) {
      typeView.isEnabled = it.isEmpty // create mode
    }

    viewModel.type.observe(this, RequestType.WORKER) {
      typeView.setCheckedPosition(if (it.isWorker) 0 else 1)
    }

    viewModel.job.observe(this, EmptyJob) {
      jobView.job = it
    }

    viewModel.skills.observe(this, emptySet()) {
      skillsView.setTags(it.map { it.title })
      skillsCard.onAction2ClickListener = if (it.isEmpty()) null else clearSkillAction
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
      detailCard.onActionClickListener = if (it.isEmpty()) null else clearDetailAction
    }

    return rootView
  }
}
