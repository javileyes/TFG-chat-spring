package es.javiergimenez.chat.config.exception


class ForbiddenException(
        message: String = "Forbidden"
) : RuntimeException(message)