package ir.hossainkhademian.jobs.screen.request.edit

import ir.hossainkhademian.jobs.data.model.*

internal interface RequestEditListener {
  fun onRequestEditDone(request: Request, type: RequestType, job: Job, skills: Collection<Skill>, detail: String)
  fun onRequestEditCancel(request: Request)
}
