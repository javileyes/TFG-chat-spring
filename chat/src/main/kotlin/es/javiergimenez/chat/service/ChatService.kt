package es.javiergimenez.chat.service

import es.javiergimenez.chat.config.exception.BadRequestException
import es.javiergimenez.chat.domain.Chat
import es.javiergimenez.chat.domain.Message
import es.javiergimenez.chat.domain.User
import es.javiergimenez.chat.repository.ChatRepository
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import javax.transaction.Transactional


@Service
@Transactional
class ChatService {

    @Autowired lateinit var chatRepository: ChatRepository
    @Autowired lateinit var userService: UserService


    fun create(channelId: String): Chat {
        if (!validateChannelId(channelId)) {
            throw BadRequestException("Cannot create a Chat with a malformed channelId")
        }

        val chat = Chat()
        chat.channelId = channelId

        val userA = userService.findById(getUserA(channelId))
        val userB = userService.findById(getUserB(channelId))

        chat.participants.add(userA)
        chat.participants.add(userB)

        return chatRepository.save(chat)
    }

    fun updateLastMessage(chatId: Long, author: String, body: String, date: DateTime) {
        val chat = chatRepository.getOne(chatId)

        chat.lastMessageAuthor = author
        chat.lastMessageBody = body
        chat.lastMessageDate = date

        chatRepository.save(chat)
    }

    fun existsByChannelId(channelId: String): Boolean {
        if (!validateChannelId(channelId)) {
            throw BadRequestException("Bad ChannelId")
        }
        return chatRepository.existsByChannelId(channelId)
    }

    fun existsById(chatId: Long): Boolean {
        return chatRepository.existsById(chatId)
    }

    fun findByChannelId(channelId: String): Chat {
        if (!validateChannelId(channelId)) {
            throw BadRequestException("Bad ChannelId")
        }
        return chatRepository.findByChannelId(channelId)
    }

    fun findByUserId(userId: Long): List<Chat> {
        val user = User()
        user.id = userId
        return chatRepository.findByParticipantsIn(user)
    }

    fun existsByIdAndParticipantsIn(chatId: Long, user: User): Boolean {
        return chatRepository.existsByIdAndParticipantsIn(chatId, user)
    }

    fun existsByChannelIdAndParticipantsIn(channelId: String, user: User): Boolean {
        return chatRepository.existsByChannelIdAndParticipantsIn(channelId, user)
    }

    companion object {

        fun validateChannelId(channelId: String): Boolean {
            val pattern = Pattern
                    .compile("user_(?<user1>[0-9]+)_user_(?<user2>[0-9]+)")
                    .matcher(channelId)

            if (pattern.matches()) {
                val user1 = java.lang.Long.parseLong(pattern.group("user1"))
                val user2 = java.lang.Long.parseLong(pattern.group("user2"))
                return user1 < user2 //&& (userId == user1 || userId == user2)
            }

            return false
        }

        fun getUserA(channelId: String): Long {
            val strings = channelId.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return java.lang.Long.parseLong(strings[1])
        }

        fun getUserB(channelId: String): Long {
            val strings = channelId.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return java.lang.Long.parseLong(strings[3])
        }

        fun chatsToModel(userId: Long, chats: List<Chat>): List<Chat> {
            chats.stream()
                    .forEach {
                        it.title = getTitleFromChat(userId, it)
                        val message = Message(
                                body = it.lastMessageBody,
                                date = it.lastMessageDate)
                        message.user = User(username = it.lastMessageAuthor)
                        it.lastMessage = message
                    }

            return chats
        }

        private fun getTitleFromChat(userId: Long, chat: Chat): String {
            if (chat.title != null) return chat.title!!

            if (chat.participants.size == 2) {
                return chat.participants.stream()
                        .reduce { userA, userB ->
                            if (userA.id != userId) userA else userB
                        }.get().username!!
            }
            return "title not found"
        }
    }


}