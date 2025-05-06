@file:OptIn(ExperimentalTypeInference::class)
// Temp thing while waiting for official arrow release of raise with context params. See this pr and replace when it moves
//https://github.com/arrow-kt/arrow/pull/3606/files#diff-3eeb9fa410c8cb29ff11d2f63e37172a6119cafb528ba8bb65fdc0a309e6d2c6
import arrow.core.NonEmptyList
import arrow.core.raise.*
import kotlin.experimental.ExperimentalTypeInference

context(raise: Raise<NonEmptyList<Error>>)
@ExperimentalRaiseAccumulateApi
inline fun <Error, A> accumulate(
    block: RaiseAccumulate<Error>.() -> A
): A = raise.accumulate(block)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <Error, A, B> Iterable<A>.mapOrAccumulate(
    @BuilderInference transform: RaiseAccumulate<Error>.(A) -> B
): List<B> = raise.mapOrAccumulate(this, transform)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <Error, A, B> Sequence<A>.mapOrAccumulate(
    @BuilderInference transform: RaiseAccumulate<Error>.(A) -> B
): List<B> = raise.mapOrAccumulate(this, transform)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <Error, A, B> NonEmptyList<A>.mapOrAccumulate(
    @BuilderInference transform: RaiseAccumulate<Error>.(A) -> B
): NonEmptyList<B> = raise.mapOrAccumulate(this, transform)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <K, Error, A, B> Map<K, A>.mapValuesOrAccumulate(
    @BuilderInference transform: RaiseAccumulate<Error>.(Map.Entry<K, A>) -> B
): Map<K, B> = raise.mapValuesOrAccumulate(this, transform)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <Error, A, B, C> zipOrAccumulate(
    @BuilderInference action1: RaiseAccumulate<Error>.() -> A,
    @BuilderInference action2: RaiseAccumulate<Error>.() -> B,
    block: (A, B) -> C
): C = raise.zipOrAccumulate(action1, action2, block)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <Error, A, B, C, D> zipOrAccumulate(
    @BuilderInference action1: RaiseAccumulate<Error>.() -> A,
    @BuilderInference action2: RaiseAccumulate<Error>.() -> B,
    @BuilderInference action3: RaiseAccumulate<Error>.() -> C,
    block: (A, B, C) -> D
): D = raise.zipOrAccumulate(action1, action2, action3, block)

context(raise: Raise<NonEmptyList<Error>>)
@RaiseDSL
inline fun <Error, A, B, C, D, E> zipOrAccumulate(
    @BuilderInference action1: RaiseAccumulate<Error>.() -> A,
    @BuilderInference action2: RaiseAccumulate<Error>.() -> B,
    @BuilderInference action3: RaiseAccumulate<Error>.() -> C,
    @BuilderInference action4: RaiseAccumulate<Error>.() -> D,
    block: (A, B, C, D) -> E
): E = raise.zipOrAccumulate(action1, action2, action3, action4, block)