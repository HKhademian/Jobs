package ir.hossainkhademian.util

import java.util.*

object Collections {
  inline fun <T> consume(res: T, call: () -> Unit): T {
    call()
    return res
  }

  inline fun consume(call: () -> Unit) =
    consume(true, call)

  inline fun <reified T> List<T>.pickRandom(min: Int = 0, max: Int = 10): Array<T> {
    val rnd = Random()
    val count = rnd.nextInt(min + max) + min
    return Array(count) { get(rnd.nextInt(size)) }
  }

  inline fun <reified T> Array<T>.pickRandom(min: Int = 0, max: Int = 10): Array<T> {
    val rnd = Random()
    val count = rnd.nextInt(min + max) + min
    return Array(count) { get(rnd.nextInt(size)) }
  }


  inline fun <R> tri(onError: R, block: () -> R) = try {
    block()
  } catch (e: Exception) {
    onError
  }

  inline fun <R> tri(onError: () -> R, block: () -> R) = try {
    block()
  } catch (e: Exception) {
    onError()
  }


  inline fun <T, R> T.tri(onError: R, block: T.() -> R) = try {
    block()
  } catch (e: Exception) {
    onError
  }

  inline fun <T, R> T.tri(onError: T.() -> R, block: T.() -> R) = try {
    block()
  } catch (e: Exception) {
    onError()
  }


  inline fun <T, R> T.trit(onError: R, block: (T) -> R) = try {
    block(this)
  } catch (e: Exception) {
    onError
  }

  inline fun <T, R> T.trit(onError: (T) -> R, block: (T) -> R) = try {
    block(this)
  } catch (e: Exception) {
    onError(this)
  }


  inline fun <T, R> itri(it: T, onError: R, block: T.() -> R) = try {
    block(it)
  } catch (e: Exception) {
    onError
  }

  inline fun <T, R> itri(it: T, onError: T.() -> R, block: T.() -> R) = try {
    block(it)
  } catch (e: Exception) {
    onError(it)
  }


  inline fun <T, R> itrit(it: T, onError: R, block: (T) -> R) = try {
    block(it)
  } catch (e: Exception) {
    onError
  }

  inline fun <T, R> itrit(it: T, onError: (T) -> R, block: (T) -> R) = try {
    block(it)
  } catch (e: Exception) {
    onError(it)
  }
}
