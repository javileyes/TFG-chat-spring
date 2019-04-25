package es.javiergimenez.chat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
data class Chat(

        @Id
        @GeneratedValue
        var id: Long? = null,
        var channelId: String? = null,

        @JsonIgnore
        @OrderBy("date")
        @OneToMany(mappedBy = "chat")
        var messages: MutableList<Message> = mutableListOf(),

        @JsonIgnore
        @ManyToMany
        @JoinTable(name = "rel_chat_user",
                joinColumns = [JoinColumn(name = "chat_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        var participants: MutableList<User> = mutableListOf(),

        @Transient
        var title: String? = null,

        @Transient
        var lastMessage: Message? = null

): Serializable