package es.javiergimenez.chat.domain

import org.joda.time.DateTime
import java.io.Serializable
import javax.persistence.*


@Entity
@Table(
        uniqueConstraints = [UniqueConstraint(columnNames = ["token"]), UniqueConstraint(columnNames = ["user_id"])]
)
data class UserToken(

        @Id
        @GeneratedValue
        var id: Long? = null,
        var token: String? = null,

        @OneToOne
        var user: User? = null,

        var date: DateTime? = null

): Serializable