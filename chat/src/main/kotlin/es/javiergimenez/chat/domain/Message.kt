package es.javiergimenez.chat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import org.springframework.data.annotation.Transient
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.io.Serializable
import java.util.*
import javax.persistence.Lob

@Table
data class Message(

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        var channelId: String? = null,

        @PrimaryKeyColumn(ordinal = 0)
        var date: DateTime? = null,

        @PrimaryKeyColumn(ordinal = 1)
        var id: UUID? = null,

        @Lob
        var body: String? = null,

        @JsonIgnore
        var chatId: Long? = null,
        @JsonIgnore
        var userId: Long? = null,
        @JsonIgnore
        var username: String? = null


): Serializable {

        @Transient
        var user: User? = null

}