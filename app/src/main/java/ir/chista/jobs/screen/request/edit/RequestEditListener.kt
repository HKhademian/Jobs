package ir.chista.jobs.screen.request.edit

import ir.chista.jobs.data.model.*

internal interface RequestEditListener {
  fun onRequestEditDone(requestId: ID)
  fun onRequestEditCancel(requestId: ID)
}
