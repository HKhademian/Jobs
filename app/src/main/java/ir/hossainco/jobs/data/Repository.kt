package ir.hossainco.jobs.data

import android.arch.lifecycle.LiveData
import ir.hossainco.jobs.data.model.Chat
import ir.hossainco.jobs.data.model.Job
import ir.hossainco.jobs.data.model.UserWithChats

interface Repository {
	fun getChatsByContact(userId: Long): LiveData<List<Chat>>
	fun getUserChats(): LiveData<List<UserWithChats>>
	fun getJobs(): LiveData<List<Job>>
}
