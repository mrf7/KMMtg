@file:OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.NonEmptySet
import arrow.core.Option
import arrow.core.Some
import arrow.core.raise.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName
import arrow.core.raise.Raise as CoreRaise
import arrow.core.raise.SingletonRaise as CoreSingletonRaise
import arrow.core.raise.ResultRaise as CoreResultRaise

typealias Raise<Error> = CoreRaise<Error>
typealias SingletonRaise<Error> = CoreSingletonRaise<Error>
typealias ResultRaise = CoreResultRaise

context(raise: Raise<Error>) @RaiseDSL
fun <Error> raise(e: Error): Nothing =
    raise.raise(e)

context(raise: Raise<Error>) @RaiseDSL
inline fun <Error> ensure(condition: Boolean, otherwise: () -> Error) {
    contract { returns() implies condition }
    raise.ensure(condition, otherwise)
}

context(raise: Raise<Error>) @RaiseDSL
inline fun <Error, B : Any> ensureNotNull(value: B?, otherwise: () -> Error): B {
    contract { returns() implies (value != null) }
    return raise.ensureNotNull(value, otherwise)
}

context(raise: Raise<Error>) @RaiseDSL
inline fun <Error, OtherError, A> withError(
    transform: (OtherError) -> Error,
    @BuilderInference block: CoreRaise<OtherError>.() -> A
): A = raise.withError(transform, block)

context(raise: Raise<Error>) @RaiseDSL
suspend fun <Error, A> Effect<Error, A>.bind(): A =
    with(raise) { bind() }

context(raise: Raise<Error>) @RaiseDSL
fun <Error, A> Either<Error, A>.bind(): A {
    contract { returns() implies (this@bind is Either.Right) }
    return with(raise) { this@bind.bind() }
}

context(raise: SingletonRaise<Error>) @RaiseDSL
fun <Error, A> Option<A>.bind(): A {
    contract { returns() implies (this@bind is Some) }
    return with(raise) { this@bind.bind() }
}

context(raise: SingletonRaise<Error>) @RaiseDSL
fun <Error, A> A?.bind(): A {
    contract { returns() implies (this@bind != null) }
    return with(raise) { this@bind.bind() }
}

context(raise: ResultRaise) @RaiseDSL
fun <Error, A> Result<A>.bind(): A {
    return with(raise) { this@bind.bind() }
}

context(raise: Raise<Error>) @RaiseDSL
@JvmName("bindAllEither")
fun <Error, K, A> Map<K, Either<Error, A>>.bindAll(): Map<K, A> =
    with(raise) { bindAll() }

context(raise: Raise<Error>) @RaiseDSL
@JvmName("bindAllEither")
fun <Error, A> Iterable<Either<Error, A>>.bindAll(): List<A> =
    with(raise) { bindAll() }

context(raise: Raise<Error>) @RaiseDSL
@JvmName("bindAllEither")
fun <Error, A> NonEmptyList<Either<Error, A>>.bindAll(): NonEmptyList<A> =
    with(raise) { bindAll() }

context(raise: Raise<Error>) @RaiseDSL
@JvmName("bindAllEither")
fun <Error, A> NonEmptySet<Either<Error, A>>.bindAll(): NonEmptySet<A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllOption")
fun <Error, K, A> Map<K, Option<A>>.bindAll(): Map<K, A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllOption")
fun <Error, A> Iterable<Option<A>>.bindAll(): List<A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllOption")
fun <Error, A> NonEmptyList<Option<A>>.bindAll(): NonEmptyList<A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllOption")
fun <Error, A> NonEmptySet<Option<A>>.bindAll(): NonEmptySet<A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllNullable")
fun <Error, K, A> Map<K, A?>.bindAll(): Map<K, A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllNullable")
fun <Error, A> Iterable<A?>.bindAll(): List<A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllNullable")
fun <Error, A> NonEmptyList<A?>.bindAll(): NonEmptyList<A> =
    with(raise) { bindAll() }

context(raise: SingletonRaise<Error>) @RaiseDSL
@JvmName("bindAllNullable")
fun <Error, A> NonEmptySet<A?>.bindAll(): NonEmptySet<A> =
    with(raise) { bindAll() }

context(raise: ResultRaise) @RaiseDSL
@JvmName("bindAllResult")
fun <Error, K, A> Map<K, Result<A>>.bindAll(): Map<K, A> =
    with(raise) { bindAll() }

context(raise: ResultRaise) @RaiseDSL
@JvmName("bindAllResult")
fun <Error, A> Iterable<Result<A>>.bindAll(): List<A> =
    with(raise) { bindAll() }

context(raise: ResultRaise) @RaiseDSL
@JvmName("bindAllResult")
fun <Error, A> NonEmptyList<Result<A>>.bindAll(): NonEmptyList<A> =
    with(raise) { bindAll() }

context(raise: ResultRaise) @RaiseDSL
@JvmName("bindAllResult")
fun <Error, A> NonEmptySet<Result<A>>.bindAll(): NonEmptySet<A> =
    with(raise) { bindAll() }