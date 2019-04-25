package es.javiergimenez.chat.repository

import es.javiergimenez.chat.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User

}