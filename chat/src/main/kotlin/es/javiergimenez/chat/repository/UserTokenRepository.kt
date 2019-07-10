package es.javiergimenez.chat.repository

import es.javiergimenez.chat.domain.UserToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTokenRepository: JpaRepository<UserToken, Long> {

    fun existsByToken(token: String): Boolean

    fun findByToken(token: String): UserToken

    fun existsByUser_Username(username: String): Boolean

    fun findByUser_Username(username: String): UserToken

}