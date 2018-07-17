package ir.hossainkhademian.jobs.screen.request.edit

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.v7.app.AlertDialog
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.dialog.JobSelectDialog
import ir.hossainkhademian.jobs.dialog.SkillSelectDialog
import ir.hossainkhademian.util.Observables.toLiveData
import ir.hossainkhademian.util.LiveDatas.observe

internal class RequestEditViewModel(
  val app: App,
  val fragment: RequestEditFragment,
  val requestId: ID
) : AndroidViewModel(app) {
  val request = Repository.Requests.findById(requestId).toLiveData()

  val type: LiveData<RequestType> = MutableLiveData()
  val job: LiveData<Job> = MutableLiveData()
  val detail: LiveData<String> = MutableLiveData()
  val skills: LiveData<Set<Skill>> = MutableLiveData()

  private val skillList = mutableSetOf<Skill>()
  private var jobSelectDialog: AlertDialog? = null
  private var skillsSelectDialog: AlertDialog? = null

  init {
    type as MutableLiveData
    job as MutableLiveData
    detail as MutableLiveData
    skills as MutableLiveData

    type.value = RequestType.WORKER
    job.value = EmptyJob
    skills.value = skillList
    detail.value = ""

    request.observe(fragment, EmptyRequest) {
      type.postValue(it.type)
      job.postValue(it.job)
      detail.postValue(it.detail)

      skillList.clear()
      skillList += it.skills
      skills.postValue(skillList)
    }
  }

  fun submit() {
    fragment.onSubmitListener(requestId)
  }

  fun cancel() {
    fragment.onCancelListener(requestId)
  }

  fun selectJob() {
    job as MutableLiveData

    jobSelectDialog?.dismiss()
    jobSelectDialog = JobSelectDialog.show(fragment.context, job.value?.id ?: emptyID) {
      job.postValue(it)
      jobSelectDialog?.dismiss()
      jobSelectDialog = null
    }
  }

  fun addSkill() {
    skills as MutableLiveData

    skillsSelectDialog?.dismiss()
    skillsSelectDialog = SkillSelectDialog.show(fragment.context, skills.value?.mapId() ?: emptyList()) {
      skillList += it
      skills.postValue(skillList)
      skillsSelectDialog?.dismiss()
      skillsSelectDialog = null
    }
  }

  fun clearSkills() {
    skills as MutableLiveData
    skillList.clear()
    skills.postValue(skillList)
  }

  fun clearDetail() {
    detail as MutableLiveData
    detail.postValue("")
  }

  fun removeSkill(skillTitle: String?) {
    skills as MutableLiveData
    skillList.removeAll { it.title == skillTitle }
    skills.postValue(skillList)
  }

  fun onDetailChanged(newDetail: String) {
    detail as MutableLiveData
    if (detail.value != newDetail)
      detail.postValue(newDetail)
  }
}
