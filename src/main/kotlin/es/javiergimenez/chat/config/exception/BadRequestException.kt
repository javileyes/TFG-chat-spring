package es.javiergimenez.chat.config.exception


class BadRequestException(
        message: String = "Bad Request"
) : RuntimeException(message)