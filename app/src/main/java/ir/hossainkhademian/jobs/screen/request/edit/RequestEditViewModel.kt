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

internal class RequestEditViewModel : BaseViewModel() {
  val request: LiveData<LocalRequest> = MutableLiveData()
  val type: LiveData<RequestType> = MutableLiveData()
  val job: LiveData<Job> = MutableLiveData()
  val detail: LiveData<String> = MutableLiveData()
  val skills: LiveData<Set<Skill>> = MutableLiveData()

  private var jobSelectDialog: AlertDialog? = null
  private var skillsSelectDialog: AlertDialog? = null
  private var disposable: Disposable? = null
  var listener: RequestEditListener? = null


  var requestId: ID
    get() = request.value?.id ?: emptyID
    set(requestId) {
      request as MutableLiveData
      type as MutableLiveData
      job as MutableLiveData
      detail as MutableLiveData
      skills as MutableLiveData

      disposable?.dispose()
      disposable = Repository.Requests.findById(requestId).subscribe {
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

  init {
    request as MutableLiveData
    type as MutableLiveData
    job as MutableLiveData
    detail as MutableLiveData
    skills as MutableLiveData

    request.value = EmptyRequest
    type.value = RequestType.WORKER
    job.value = EmptyJob
    skills.value = mutableSetOf()
    detail.value = ""
  }

  fun submit() {
    val skillSet = skills.value ?: emptySet()

    listener?.onRequestEditSubmit(
      request.value ?: EmptyRequest,
      type.value ?: RequestType.WORKER,
      job.value ?: EmptyJob,
      skills.value ?: skillSet,
      detail.value ?: ""
    )
  }

  fun cancel() {
    listener?.onRequestEditCancel(
      request.value ?: EmptyRequest
    )
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
