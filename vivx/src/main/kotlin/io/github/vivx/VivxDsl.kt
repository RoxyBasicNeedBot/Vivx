package io.github.vivx

/**
 * Marks the Vivx DSL scopes to prevent implicit scope leakage across nested hierarchies.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class VivxDsl
