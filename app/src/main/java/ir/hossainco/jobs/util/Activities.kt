package ir.hossainco.jobs.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import java.io.Serializable

val <T : Context> T.context get() = this
val <T : Activity> T.activity get() = this

inline fun <reified T : Any> Activity.launchActivity(
	requestCode: Int = -1,
	options: Bundle? = null,
	vararg extras: Pair<String, Any>,
	noinline init: Intent.() -> Unit = {}) {

	val intent = newIntent<T>(this)
	intent.putExtras(*extras)
	intent.init()

//	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		startActivityForResult(intent, requestCode, options)
	else
		startActivityForResult(intent, requestCode)
}

inline fun <reified T : Any> Context.launchActivity(
	options: Bundle? = null,
	vararg extras: Pair<String, Any>,
	noinline init: Intent.() -> Unit = {}) {

	val intent = newIntent<T>(this)
	intent.putExtras(*extras)
	intent.init()

//	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		startActivity(intent, options)
	else
		startActivity(intent)
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun bundle(vararg pairs: Pair<String, Any>): Bundle {
	val bundle = Bundle()
	pairs.forEach {
		when (it.second) {
			is String -> bundle.putString(it.first, it.second as String)
			is CharSequence -> bundle.putCharSequence(it.first, it.second as CharSequence)
			is Double -> bundle.putDouble(it.first, it.second as Double)
			is Float -> bundle.putFloat(it.first, it.second as Float)
			is Long -> bundle.putLong(it.first, it.second as Long)
			is Int -> bundle.putInt(it.first, it.second as Int)
			is Short -> bundle.putShort(it.first, it.second as Short)
			is Byte -> bundle.putByte(it.first, it.second as Byte)
			is Char -> bundle.putChar(it.first, it.second as Char)
			is DoubleArray -> bundle.putDoubleArray(it.first, it.second as DoubleArray)
			is FloatArray -> bundle.putFloatArray(it.first, it.second as FloatArray)
			is LongArray -> bundle.putLongArray(it.first, it.second as LongArray)
			is IntArray -> bundle.putIntArray(it.first, it.second as IntArray)
			is ShortArray -> bundle.putShortArray(it.first, it.second as ShortArray)
			is ByteArray -> bundle.putByteArray(it.first, it.second as ByteArray)
			is CharArray -> bundle.putCharArray(it.first, it.second as CharArray)
			is Bundle -> bundle.putBundle(it.first, it.second as Bundle)
			is Parcelable -> bundle.putParcelable(it.first, it.second as Parcelable)
			is Serializable -> bundle.putSerializable(it.first, it.second as java.io.Serializable)
			is IBinder -> bundle.putBinder(it.first, it.second as IBinder)
			is Size -> bundle.putSize(it.first, it.second as Size)
			is SizeF -> bundle.putSizeF(it.first, it.second as SizeF)
			else -> throw UnsupportedOperationException()
		}
	}
	return bundle
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Intent.putExtras(vararg pairs: Pair<String, Any>) =
	pairs.forEach {
		when (it.second) {
			is String -> putExtra(it.first, it.second as String)
			is CharSequence -> putExtra(it.first, it.second as CharSequence)
			is Double -> putExtra(it.first, it.second as Double)
			is Float -> putExtra(it.first, it.second as Float)
			is Long -> putExtra(it.first, it.second as Long)
			is Int -> putExtra(it.first, it.second as Int)
			is Short -> putExtra(it.first, it.second as Short)
			is Byte -> putExtra(it.first, it.second as Byte)
			is Char -> putExtra(it.first, it.second as Char)
			is DoubleArray -> putExtra(it.first, it.second as DoubleArray)
			is FloatArray -> putExtra(it.first, it.second as FloatArray)
			is LongArray -> putExtra(it.first, it.second as LongArray)
			is IntArray -> putExtra(it.first, it.second as IntArray)
			is ShortArray -> putExtra(it.first, it.second as ShortArray)
			is ByteArray -> putExtra(it.first, it.second as ByteArray)
			is CharArray -> putExtra(it.first, it.second as CharArray)
			is Bundle -> putExtra(it.first, it.second as Bundle)
			is Parcelable -> putExtra(it.first, it.second as Parcelable)
			is Serializable -> putExtra(it.first, it.second as java.io.Serializable)
			else -> throw UnsupportedOperationException()
		}
	}

inline fun <reified T : Any> newIntent(context: Context): Intent =
	Intent(context, T::class.java)
