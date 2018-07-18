/**
 * https://proandroiddev.com/kotlin-coroutines-vs-rxjava-an-initial-performance-test-68160cfc6723
 * https://medium.com/@chrisbanes/rxjava-to-kotlin-coroutines-1204c896a700
 */
package ir.chista.util

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.CoroutineContext

suspend fun <A, B> Collection<A>.parallelMap(
  context: CoroutineContext = CommonPool,
  block: suspend (A) -> B
): Collection<B> {
  return map {
    // Use async to start a coroutine for each item
    async(context) {
      block(it)
    }
  }.map {
    // We now have a map of Deferred<T> so we await() each
    it.await()
  }
}
