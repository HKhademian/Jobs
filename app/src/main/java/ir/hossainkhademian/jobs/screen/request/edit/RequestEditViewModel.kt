package ir.hossainkhademian.jobs.screen.request.edit

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.v7.app.AlertDialog
import io.reactivex.disposables.Disposable
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.dialog.JobSelectDialog
import ir.hossainkhademian.jobs.dialog.SkillSelectDialog
import ir.hossainkhademian.jobs.util.BaseViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.DisposableHandle
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

internal class RequestEditViewModel : BaseViewModel() {
  val request: LiveData<LocalRequest> = MutableLiveData()
  val type: LiveData<RequestType> = MutableLiveData()
  val job: LiveData<Job> = MutableLiveData()
  val detail: LiveData<String> = MutableLiveData()
  val skills: LiveData<Set<Skill>> = MutableLiveData()
  val isSubmiting: LiveData<Boolean> = MutableLiveData()
  val isSavable: LiveData<Boolean> = MutableLiveData()

  private var jobSelectDialog: AlertDialog? = null
  private var skillsSelectDialog: AlertDialog? = null
  private var disposableRequestId: Disposable? = null
  private var disposableSubmit: DisposableHandle? = null
  var listener: RequestEditListener? = null


  var requestId: ID
    get() = request.value?.id ?: emptyID
    set(requestId) {
      request as MutableLiveData
      type as MutableLiveData
      job as MutableLiveData
      detail as MutableLiveData
      skills as MutableLiveData
      isSubmiting as MutableLiveData

      disposableRequestId?.dispose()
      disposableRequestId = Repository.Requests.findById(requestId).subscribe {
        val item = it ?: EmptyRequest
        val skillSet = skills.value as? MutableSet ?: mutableSetOf()

        request.postValue(item)
        type.postValue(item.type)
        job.postValue(item.job)
        detail.postValue(item.detail)

        skillSet.clear()
        skillSet += item.skills
        skills.postValue(skillSet)
      }
    }

  fun init(requestId: ID = emptyID) {
    request as MutableLiveData
    type as MutableLiveData
    job as MutableLiveData
    detail as MutableLiveData
    skills as MutableLiveData
    isSubmiting as MutableLiveData

    // request.value = EmptyRequest
    type.value = RequestType.WORKER
    job.value = EmptyJob
    skills.value = mutableSetOf()
    detail.value = ""
    isSubmiting.value = false

    this.requestId = requestId
  }

  fun submit() {
    isSubmiting as MutableLiveData

    val request = request.value ?: EmptyRequest
    val type = type.value ?: RequestType.WORKER
    val job = job.value ?: EmptyJob
    val skills = skills.value ?: emptyList<Skill>()
    val detail = detail.value ?: ""

    isSubmiting.postValue(true)
    disposableSubmit?.dispose()
    disposableSubmit = launch(CommonPool + CoroutineExceptionHandler { _, ex -> postError(ex) }) {
      val newRequest = Repository.Requests.update(request.id, type, job.id, skills.mapId(), detail)

      launch(UI) {
        listener?.onRequestEditDone(newRequest.id)
      }
    }.invokeOnCompletion {
      disposableSubmit = null
      isSubmiting.postValue(false)
    }
  }

  fun cancel() {
    listener?.onRequestEditCancel(requestId)
  }

  fun selectJob() {
    job as MutableLiveData

    try {
      jobSelectDialog?.dismiss()
    } catch (e: Exception) {
    }

    postActivityTask { activity ->
      jobSelectDialog = JobSelectDialog.show(activity, job.value?.id ?: emptyID) {
        job.postValue(it)
        jobSelectDialog?.dismiss()
        jobSelectDialog = null
      }
    }
  }

  fun addSkill() {
    skills as MutableLiveData

    try {
      skillsSelectDialog?.dismiss()
    } catch (e: Exception) {
    }

    postActivityTask { activity ->
      skillsSelectDialog = SkillSelectDialog.show(activity, skills.value?.mapId() ?: emptyList()) {
        val skillSet = skills.value as? MutableSet ?: mutableSetOf()
        skillSet += it
        skills.postValue(skillSet)
        skillsSelectDialog?.dismiss()
        skillsSelectDialog = null
      }
    }
  }

  fun clearSkills() {
    skills as MutableLiveData
    val skillSet = skills.value as? MutableSet ?: mutableSetOf()

    skillSet.clear()
    skills.postValue(skillSet)
  }

  fun clearDetail() {
    detail as MutableLiveData
    detail.postValue("")
  }

  fun removeSkill(skillTitle: String?) {
    skills as MutableLiveData
    val skillSet = skills.value as? MutableSet ?: mutableSetOf()

    skillSet.removeAll { it.title == skillTitle }
    skills.postValue(skillSet)
  }

  fun onDetailChanged(newDetail: String) {
    detail as MutableLiveData
    if (detail.value != newDetail)
      detail.postValue(newDetail)
  }
}
