package es.javiergimenez.chat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import java.io.Serializable
import javax.persistence.*

@Entity
class Message(

        @Id
        @GeneratedValue
        var id: Long? = null,

        @Lob
        var body: String? = null,

        @JsonIgnore
        @ManyToOne
        var chat: Chat? = null,

        @ManyToOne
        var user: User? = null,

        var date: DateTime? = null

): Serializable