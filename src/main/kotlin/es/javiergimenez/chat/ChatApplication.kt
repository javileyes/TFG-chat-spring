package es.javiergimenez.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class ChatApplication

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
