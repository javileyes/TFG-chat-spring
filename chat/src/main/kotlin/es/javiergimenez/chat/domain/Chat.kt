package es.javiergimenez.chat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import java.io.Serializable
import javax.persistence.*

@Entity
data class Chat(

        @Id
        @GeneratedValue
        var id: Long? = null,
        var channelId: String? = null,

        @JsonIgnore
        @ManyToMany
        @JoinTable(name = "rel_chat_user",
                joinColumns = [JoinColumn(name = "chat_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        var participants: MutableList<User> = mutableListOf(),

        // todo probar si sql acepta un onetomany con cassandra
        @Lob
        @JsonIgnore
        var lastMessageBody: String? = null,
        @JsonIgnore
        var lastMessageDate: DateTime? = null,
        @JsonIgnore
        var lastMessageAuthor: String? = null,

        @Transient
        var title: String? = null,

        @Transient
        var lastMessage: Message? = null


): Serializable