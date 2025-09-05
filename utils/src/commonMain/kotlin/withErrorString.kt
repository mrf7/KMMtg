@file:OptIn(ExperimentalTypeInference::class)

import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL
import arrow.core.raise.recover
import arrow.core.raise.withError
import kotlin.experimental.ExperimentalTypeInference

/**
 * Runs the [block] in the required raise context and converts any raised error to a string, optionally transforming it
 */
@RaiseDSL
context(raise: Raise<String>)
inline fun <OtherError, A> withErrorString(
    transform: OtherError.() -> String = { toString() },
    @BuilderInference block: Raise<OtherError>.() -> A,
): A = raise.withError({ it.transform() }, block)

/**
 * Runs the [block] and just throws an exception if an error is raised, optionally
 * transforming the error message using the [transform] function.
 */
@RaiseDSL
inline fun <E, R> boom(transform: E.() -> String = { toString() }, block: context(Raise<E>)() -> R): R = recover(block) { error(it.transform()) }
