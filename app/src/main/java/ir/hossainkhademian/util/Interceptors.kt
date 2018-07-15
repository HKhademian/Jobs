package ir.hossainkhademian.util

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

object Interceptors {
  const val CACHE_TIME_ZERO = 1000 * 0 /* ms */
  const val CACHE_TIME_NANO = 1000 * 10 /* ms */
  const val CACHE_TIME_TINY = 1000 * 20 /* ms */
  const val CACHE_TIME_MINY = 1000 * 30 /* ms */
  const val CACHE_TIME_NORM = 1000 * 60 /* ms */
  const val CACHE_TIME_HOUR = 1000 * 60 * 60 /* ms */
  const val CACHE_TIME_ADAY = 1000 * 60 * 60 * 24 /* ms */
  const val CACHE_TIME_WEEK = 1000 * 60 * 60 * 24 * 7 /* ms */
  const val CACHE_TIME_MONS = 1000 * 60 * 60 * 24 * 30 /* ms */
  const val CACHE_TIME_YEAR = 1000 * 60 * 60 * 24 * 365 /* ms */

  private const val DEF_CACHE_STALE = CACHE_TIME_ADAY.toString()
  private const val DEF_CACHE_FRESH = CACHE_TIME_NORM.toString()

  const val HEADER_CACHE = "android-cache"
  const val HEADER_CACHE_FRESH = "android-cache-min"
  const val HEADER_TIMEOUT_CONNECTION = "android-timeout-connection"
  const val HEADER_TIMEOUT_READ = "android-timeout-read"
  const val HEADER_TIMEOUT_WRITE = "android-timeout-write"

  private fun request(request: Request, init: (Request.Builder.() -> Unit) = { }) =
    request.newBuilder()
      .removeHeader(HEADER_CACHE_FRESH)
      .removeHeader(HEADER_CACHE)
      .removeHeader(HEADER_TIMEOUT_CONNECTION)
      .removeHeader(HEADER_TIMEOUT_READ)
      .removeHeader(HEADER_TIMEOUT_WRITE)
      .apply(init)
      .build()

  private fun offlineRequest(request: Request, time: String? = null) = request(request) {
    header("Cache-Control", "only-if-cached, max-stale=${time
      ?: DEF_CACHE_STALE}")
  }

  private fun onlineRequest(request: Request) =
    request(request) {
      header("Cache-Control", "no-cache")
    }

  private fun cacheResponse(response: Response, time: String? = null) =
    response.newBuilder()
      .removeHeader("Pragma")
      .header("Cache-Control", "max-age=${time ?: DEF_CACHE_FRESH}")
      .build()


  private fun onlineChain(chain: Interceptor.Chain): Interceptor.Chain {
    val request = chain.request()
    val timeOutConnection = request.header(HEADER_TIMEOUT_CONNECTION)?.toIntOrNull()
      ?: chain.connectTimeoutMillis()
    val timeOutRead = request.header(HEADER_TIMEOUT_READ)?.toIntOrNull()
      ?: chain.readTimeoutMillis()
    val timeOutWrite = request.header(HEADER_TIMEOUT_WRITE)?.toIntOrNull()
      ?: chain.writeTimeoutMillis()
    return chain
      .withConnectTimeout(timeOutConnection, TimeUnit.MILLISECONDS)
      .withReadTimeout(timeOutRead, TimeUnit.MILLISECONDS)
      .withWriteTimeout(timeOutWrite, TimeUnit.MILLISECONDS)
  }

  class CacheResponseNetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response? {
      if (chain == null) return null
      val request = chain.request()
      val cache = request.header(HEADER_CACHE)
      val cacheFresh = request.header(HEADER_CACHE_FRESH)

      val response = chain.proceed(onlineRequest(request))

      return if (cache == null) response
      else cacheResponse(response, cacheFresh)
    }
  }

  class CacheResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response? {
      if (chain == null) return null
      val request = chain.request()
      val cache = request.header(HEADER_CACHE)
      val cacheFresh = request.header(HEADER_CACHE_FRESH)

      if (cacheFresh != null) {
        val response = chain.proceed(offlineRequest(request, cacheFresh))
        if (response.isSuccessful)
          return response
      }

      try {
        val onlinechain = onlineChain(chain)
        val response = onlinechain.proceed(request)
        if (response.isSuccessful)
          return response
      } catch (e: Exception) {
        e.printStackTrace()
      }

      if (cache != null) {
        val response = chain.proceed(offlineRequest(request, cache))
        if (response.isSuccessful)
          return response
      }

      throw IOException("cannot connect to server!")
    }
  }


//	class MixedRequestInterceptor : Interceptor {
//		override fun intercept(chain: Interceptor.Chain?): Response? {
//			if (chain == null) return null
//			val request = chain.request()
//			val cache = request.header(HEADER_CACHE)
//			val cacheFresh = request.header(HEADER_CACHE_FRESH)
//
//			if (cacheFresh != null) {
//				val response = chain.proceed(offlineRequest(request, cacheFresh))
//				if (response.isSuccessful)
//					return response
//			}
//
//			try {
//				val newRequest = onlineRequest(request)
//				val response = chain.proceed(newRequest)
//				if (response.isSuccessful)
//					return if (cache == null) cache
//					else cacheResponse(response, cache)
//			} catch (e: Exception) {
//			}
//
//			if (cache != null)
//				return chain.proceed(offlineRequest(request, cache))
//
//			return null
//		}
//	}

//	class OfflineLastInterceptor : Interceptor {
//		override fun intercept(chain: Interceptor.Chain?): Response? {
//			if (chain == null) return null
//			val request = chain.request()
//			val cache = request.header(HEADER_CACHE)
//			val cacheFresh = request.header(HEADER_CACHE_FRESH)
//
//			try {
//				val response = chain.proceed(onlineRequest(request))
//				return if (cache == null) response
//				else cacheResponse(response, cacheFresh)
//			} catch (e: Exception) {
//			}
//
//			return chain.proceed(offlineRequest(request, cache))
//		}
//	}

//	class OfflineFirstInterceptor : Interceptor {
//		override fun intercept(chain: Interceptor.Chain?): Response? {
//			if (chain == null) return null
//			val request = chain.request()
//			val cache = request.header(HEADER_CACHE)
//			val cacheFresh = request.header(HEADER_CACHE_FRESH)
//
//			if (cache != null) {
//				val newRequest = offlineRequest(request, cache)
//				val response = chain.proceed(newRequest)
//				if (response.isSuccessful)
//					return response
//			}
//			val response = chain.proceed(onlineRequest(request))
//			return if (cache == null) response
//			else cacheResponse(response, cacheFresh)
//		}
//	}
}
