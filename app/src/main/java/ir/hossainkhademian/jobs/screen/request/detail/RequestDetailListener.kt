package ir.hossainkhademian.jobs.screen.request.detail

import ir.hossainkhademian.jobs.data.model.*

internal interface RequestDetailListener {
  fun onRequestDetailEdit(requestId: ID)
  fun onRequestDetailChat(requestId: ID, userId: ID)
  fun onRequestDetailCloseDone(requestId: ID)
}
