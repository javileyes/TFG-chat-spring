package es.javiergimenez.chat.web.rest

import es.javiergimenez.chat.domain.Chat
import es.javiergimenez.chat.security.aspect.Security
import es.javiergimenez.chat.service.ChatService
import es.javiergimenez.chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@Security
@RestController
class ChatRest {

    @Autowired lateinit var chatService: ChatService
    @Autowired lateinit var userService: UserService


    @GetMapping("/api/chats")
    fun getChats(): ResponseEntity<List<Chat>> {
        val userId = userService.whoAmI().id!!
        val chats = chatService.findByUserId(userId)

        return ResponseEntity.status(HttpStatus.OK).body(ChatService.chatsToModel(userId, chats))
    }


}