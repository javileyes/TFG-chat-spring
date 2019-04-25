package es.javiergimenez.chat.service

import es.javiergimenez.chat.config.exception.BadRequestException
import es.javiergimenez.chat.config.exception.NotFoundException
import es.javiergimenez.chat.domain.Message
import es.javiergimenez.chat.repository.MessageRepository
import es.javiergimenez.chat.service.firebase.FirebaseRetrofit
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
@Transactional
class MessageService {

    @Autowired lateinit var messageRepository: MessageRepository
    @Autowired lateinit var chatService: ChatService
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var firebaseTool: FirebaseRetrofit


    fun mock() {
        if (messageRepository.findAll().isEmpty()) {
            create("user_1_user_2", "Hola Jane", 1)
            create("user_1_user_2", "Que tal estas", 1)
            create("user_1_user_2", "Bien y tu?", 2)
        }
    }

    fun create(channelId: String, body: String, userId: Long): Message {

        val chat = if (!chatService.existsByChannelId(channelId)) {
            if (userId != ChatService.getUserA(channelId) && userId != ChatService.getUserB(channelId)) {
                throw BadRequestException("User $userId can't send a message to chat $channelId")
            }
            chatService.create(channelId)
        } else {
            chatService.findByChannelId(channelId)
        }

        var message = Message()
        message.body = body
        message.chat = chat
        message.user = userService.findById(userId)
        message.date = DateTime.now()
        message = messageRepository.save(message)

//        firebaseTool.send(chat.participants.stream().map { user -> user.id!! }.collect(Collectors.toList()), message)
        firebaseTool.send(chat.participants, message)

        return message
    }

    fun findByChatId(chatId: Long): List<Message> {
        if (!chatService.existsById(chatId)) {
            throw NotFoundException()
        }
        return messageRepository.findByChat_Id(chatId)
    }

    fun findByChannelId(channelId: String): List<Message> {
        if (!chatService.existsByChannelId(channelId)) {
            throw NotFoundException()
        }
        return messageRepository.findByChat_ChannelId(channelId)
    }


}