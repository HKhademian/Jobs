@file:Suppress("PackageDirectoryMismatch")

package ir.hossainkhademian.jobs.data.api

import ir.hossainkhademian.jobs.data.model.JobData
import ir.hossainkhademian.jobs.data.api.model.toData
import retrofit2.Call
import retrofit2.mock.Calls

object JobMock : JobService {
  override fun list(): Call<List<JobData>> {
    MockApiStorage.fakeWait()

    return Calls.response(MockApiStorage.jobs.items.toData())
  }
}
