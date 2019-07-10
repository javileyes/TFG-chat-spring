package es.javiergimenez.chat.web.rest

import es.javiergimenez.chat.domain.Message
import es.javiergimenez.chat.security.ChatSecurity
import es.javiergimenez.chat.security.aspect.Authorize
import es.javiergimenez.chat.security.aspect.Security
import es.javiergimenez.chat.service.MessageService
import es.javiergimenez.chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Security
@RestController
class MessageRest {

    @Autowired lateinit var messageService: MessageService
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var chatSecurity: ChatSecurity


    @Authorize("@chatSecurity.isMyChat(#channelId)")
    @GetMapping("/api/chats/{channelId}/messages")
    fun getMessages(@PathVariable channelId: String): ResponseEntity<List<Message>> {
        val messages = messageService.findByChannelId(channelId)
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageService.messagesToModel(messages))
    }

    @Authorize("@chatSecurity.isMyChat(#channelId)")
    @PostMapping("/api/chats/{channelId}/messages")
    fun postMessage(@PathVariable channelId: String, @RequestParam body: String): ResponseEntity<Message> {
        val message = messageService.create(channelId, body, userService.whoAmI().id!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageService.messageToModel(message))
    }


}