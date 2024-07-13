package ua.oshevchuk.browserrequeststracker.common


import java.util.concurrent.CancellationException

inline fun <T, R> T.executeSafely(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}