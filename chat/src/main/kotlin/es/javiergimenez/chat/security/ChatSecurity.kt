package es.javiergimenez.chat.security

import es.javiergimenez.chat.service.ChatService
import es.javiergimenez.chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ChatSecurity {

    @Autowired lateinit var userService: UserService
    @Autowired lateinit var chatService: ChatService


    fun isMyChat(chatId: Long): Boolean {
        val user = userService.whoAmI()
        return chatService.existsByIdAndParticipantsIn(chatId, user)
    }

    fun isMyChat(channelId: String): Boolean {
        val user = userService.whoAmI()

        if (!chatService.existsByChannelId(channelId)) {
            return true
        } else {
            return chatService.existsByChannelIdAndParticipantsIn(channelId, user)
        }
    }


}