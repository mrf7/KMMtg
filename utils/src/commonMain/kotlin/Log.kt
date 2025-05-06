import co.touchlab.kermit.Logger

inline fun <T> T.alsoLog(block: (T) -> String = { this.toString() }) = also { Logger.d(block(this)) }
