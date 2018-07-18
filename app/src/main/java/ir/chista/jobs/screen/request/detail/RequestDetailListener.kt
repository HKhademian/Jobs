package ir.chista.jobs.screen.request.detail

import ir.chista.jobs.data.model.*

internal interface RequestDetailListener {
  fun onRequestDetailEdit(requestId: ID)
  fun onRequestDetailChat(requestId: ID, userId: ID)
  fun onRequestDetailCloseDone(requestId: ID)
}
