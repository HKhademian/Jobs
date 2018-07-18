package ir.hossainkhademian.jobs.data.model

import android.arch.lifecycle.Transformations.map
import io.reactivex.Observable
import java.util.*

//import java.util.Random
//private val rand by lazy(::Random)
//typealias ID = Long
//val emptyID: ID get() = 0L
//val generateID: ID get() = rand.nextLong()

typealias ID = String

val emptyID: ID get() = ""
val generateID: ID get() = UUID.randomUUID().toString()
val IdModel.idStr
  get() = id.toString()

val ID.isEmpty get() = this == emptyID
val ID.isNotEmpty get() = this != emptyID

val IdModel.isEmpty get() = id.isEmpty
val IdModel.isNotEmpty get() = id.isNotEmpty

interface IdModel {
  val id: ID
}

fun <T : IdModel> Iterable<T>.mapId() = map { it.id }
fun <T : IdModel> Iterable<T>.getById(id: ID) = first { it.id == id }
fun <T : IdModel> Iterable<T>.findById(id: ID) = find { it.id == id }
fun <T : IdModel> Iterable<T>.findById(id: ID, default: T) = find { it.id == id } ?: default
fun <T : IdModel> Iterable<T>.filterById(id: ID) = filter { it.id == id }
fun <T : IdModel> Iterable<T>.filterById(ids: Collection<ID>) = filter { ids.contains(it.id) }

fun <T : IdModel, C : Iterable<T>> Observable<C>.mapId() = map { it.mapId() }
fun <T : IdModel, C : Iterable<T>> Observable<C>.getById(id: ID) = map { it.getById(id) }
fun <T : IdModel, C : Iterable<T>> Observable<C>.findById(id: ID) = map { it.findById(id) }
fun <T : IdModel, C : Iterable<T>> Observable<C>.findById(id: ID, default: T) = map { it.findById(id, default) }
fun <T : IdModel, C : Iterable<T>> Observable<C>.filterById(id: ID) = map { it.filterById(id) }
fun <T : IdModel, C : Iterable<T>> Observable<C>.filterById(ids: Collection<ID>) = map { it.filterById(ids) }
