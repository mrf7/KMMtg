import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

inline fun <T> T.alsoLog(level: (T) -> Severity = { Severity.Debug }, block: (T) -> String = { this.toString() }) = also { Logger.log(level(this), Logger.tag, null, block(this)) }

inline fun <T> T.alsoLog(level: Severity, block: (T) -> String = { this.toString() }) = alsoLog({ level }, block)
