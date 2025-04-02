import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Message
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ComposeLogger : LogWriter() {
    private val _logs = MutableStateFlow(emptyList<LogEntry>())
    val logs = _logs.asStateFlow()

    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?,
    ) {
        _logs.update { old ->
            val formatted = DefaultFormatter.formatMessage(severity, Tag(tag), Message(message))
            val throwableStacktrace = throwable?.stackTraceToString()
            old + LogEntry(severity, listOfNotNull(formatted, throwableStacktrace).joinToString("\n"))
        }
    }
}

data class LogEntry(
    val level: Severity,
    val message: String,
)
