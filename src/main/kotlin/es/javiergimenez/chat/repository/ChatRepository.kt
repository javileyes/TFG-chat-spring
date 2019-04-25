package es.javiergimenez.chat.repository

import es.javiergimenez.chat.domain.Chat
import es.javiergimenez.chat.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ChatRepository: JpaRepository<Chat, Long> {

    fun findByChannelId(channelId: String): Chat

    fun existsByChannelId(channelId: String): Boolean

    fun findByParticipantsIn(user: User): List<Chat>

    fun existsByIdAndParticipantsIn(chatId: Long, user: User): Boolean

    fun existsByChannelIdAndParticipantsIn(channelId: String, user: User): Boolean

}