package es.javiergimenez.chat.service

import com.datastax.driver.core.utils.UUIDs
import es.javiergimenez.chat.config.exception.BadRequestException
import es.javiergimenez.chat.config.exception.NotFoundException
import es.javiergimenez.chat.domain.Message
import es.javiergimenez.chat.domain.User
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
        message.id = UUIDs.timeBased()
        message.body = body
        message.chatId = chat.id
        message.channelId = chat.channelId
        val user = userService.findById(userId)
        message.userId = user.id
        message.username = user.username
        message.date = DateTime.now()
        message = messageRepository.save(message)

        chatService.updateLastMessage(chat.id!!, message.username!!, message.body!!, message.date!!)

        firebaseTool.send(chat.participants, message)

        return message
    }

    fun findByChannelId(channelId: String): List<Message> {
        if (!chatService.existsByChannelId(channelId)) {
            throw NotFoundException()
        }
//        return messageRepository.findByChannelId(channelId)
        return messageRepository.findByChannelIdOrderByDateDesc(channelId)
    }

    companion object {
        fun messageToModel(message: Message): Message {
            message.user = User(message.userId, message.username)
            return message
        }

        fun messagesToModel(messages: List<Message>): List<Message> {
            messages.forEach {
                it.user = User(it.userId, it.username)
            }
            return messages
        }
    }


}