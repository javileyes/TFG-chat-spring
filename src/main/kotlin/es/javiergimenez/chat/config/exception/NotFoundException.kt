package es.javiergimenez.chat.config.exception


class NotFoundException(
        message: String = "Not found"
) : RuntimeException(message)