package es.javiergimenez.chat.repository

import es.javiergimenez.chat.domain.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository: JpaRepository<Message, Long> {

    fun findByChat_Id(chatId: Long): List<Message>

    fun findByChat_ChannelId(channelId: String): List<Message>

}