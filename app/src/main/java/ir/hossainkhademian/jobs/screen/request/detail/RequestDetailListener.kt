package ir.hossainkhademian.jobs.screen.request.detail

import ir.hossainkhademian.jobs.data.model.*

internal interface RequestDetailListener {
  fun onRequestDetailEdit(request: Request)
  fun onRequestDetailChat(request: Request, user: User)
  fun onRequestDetailCancel(request: Request)
}
