@file:Suppress("PackageDirectoryMismatch")

package ir.chista.jobs.data.api

import ir.chista.jobs.data.model.JobData
import ir.chista.jobs.data.api.model.toData
import retrofit2.Call
import retrofit2.mock.Calls

object JobMockService : JobService {
  override fun list(): Call<List<JobData>> {
    MockApiStorage.fakeWait()

    return Calls.response(MockApiStorage.jobs.items.toData())
  }
}
