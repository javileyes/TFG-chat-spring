package es.javiergimenez.chat.config

import es.javiergimenez.chat.service.MessageService
import es.javiergimenez.chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class DatabaseConfig {

    @Autowired lateinit var userService: UserService
    @Autowired lateinit var messageService: MessageService


    @PostConstruct
    fun init() {
        userService.mock()
        messageService.mock()
    }

}