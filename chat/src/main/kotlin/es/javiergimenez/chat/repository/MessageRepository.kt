package es.javiergimenez.chat.repository

import es.javiergimenez.chat.domain.Message
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface MessageRepository: CassandraRepository<Message, UUID> {

    fun findByChannelIdOrderByDateDesc(channelId: String): List<Message>

}