package ir.hossainkhademian.jobs.screen.request.edit

import ir.hossainkhademian.jobs.data.model.*

internal interface RequestEditListener {
  fun onRequestEditDone(requestId: ID)
  fun onRequestEditCancel(requestId: ID)
}
