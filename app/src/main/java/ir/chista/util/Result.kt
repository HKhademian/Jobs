package ir.chista.util

sealed class Result<T> {
  companion object {
    fun <T> success(result: T) = SuccessResult(result)
    fun <T> error(error: Throwable) = ErrorResult<T>(error.message
      ?: "", error)

    fun <T> error(message: String) = ErrorResult<T>(message)
    fun <T> empty() = EmptyResult<T>()

    fun <T> create(response: retrofit2.Response<T>): Result<T> {
      return if (response.isSuccessful) {
        val body = response.body()
        if (body == null || response.code() == 204)
          EmptyResult()
        else
          SuccessResult(body)
      } else {
        val msg = response.errorBody()?.string()
        val errorMsg = if (msg.isNullOrEmpty())
          response.message()
        else
          msg
        ErrorResult(errorMsg ?: "unknown error")
      }
    }
  }

  class EmptyResult<T>
  internal constructor()
    : Result<T>()

  data class SuccessResult<T>
  internal constructor(val data: T)
    : Result<T>()

  data class ErrorResult<T>
  internal constructor(val message: String, val throwable: Throwable? = null)
    : Result<T>()
}

