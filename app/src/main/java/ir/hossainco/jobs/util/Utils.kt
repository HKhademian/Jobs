package ir.hossainkhademian.myarch.util

import java.util.*

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
