package es.javiergimenez.chat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(
        uniqueConstraints = [UniqueConstraint(columnNames = ["username"])]
)
data class User(

        @Id
        @GeneratedValue
        var id: Long? = null,
        var username: String? = null,

        @JsonIgnore
        var password: String? = null,

        @JsonIgnore
        @ManyToMany(mappedBy = "participants")
        var chats: MutableList<Chat> = mutableListOf(),

        @JsonIgnore
        @OneToOne(mappedBy = "user")
        var userToken: UserToken? = null

): Serializable {

    @PreRemove
    fun preRemove() {
        for (chat: Chat in chats) {
            chat.participants.remove(this)
        }
        chats.clear()

        if (userToken != null) {
            userToken!!.user = null
            userToken = null
        }

    }

}
