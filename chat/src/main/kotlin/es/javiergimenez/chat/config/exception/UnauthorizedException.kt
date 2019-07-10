package es.javiergimenez.chat.config.exception


class UnauthorizedException(
        message: String = "Unauthorized"
) : RuntimeException(message)