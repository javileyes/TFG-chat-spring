package es.javiergimenez.chat.config.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController


@ControllerAdvice(annotations = [RestController::class])
class ExceptionConfig {

    @ExceptionHandler(BadRequestException::class)
    fun badRequestException(e: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<Any>(e.message)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedException(e: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(e.message)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun forbiddenException(e: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body<Any>(e.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundException(e: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body<Any>(e.message)
    }

}

