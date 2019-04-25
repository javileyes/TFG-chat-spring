package es.javiergimenez.chat.security.aspect


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorize(
        val value: String
)
