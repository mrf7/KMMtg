@file:OptIn(ExperimentalTypeInference::class)

import arrow.core.raise.RaiseDSL
import arrow.core.raise.withError
import kotlin.experimental.ExperimentalTypeInference

context(raise: Raise<String>) @RaiseDSL
inline fun <OtherError, A> withErrorString(
    @BuilderInference block: Raise<OtherError>.() -> A
): A = raise.withError({ it.toString() }, block)
