package es.javiergimenez.chat.service

import es.javiergimenez.chat.config.exception.BadRequestException
import es.javiergimenez.chat.config.exception.NotFoundException
import es.javiergimenez.chat.domain.User
import es.javiergimenez.chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
@Transactional
class UserService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var userTokenService: UserTokenService;


    fun mock() {
        if (userRepository.findAll().isEmpty()) {
            var user = User()
            user.username = "john"
            user.password = "1234"
            create(user)

            user = User()
            user.username = "jane"
            user.password = "1234"
            create(user)

            user = User()
            user.username = "mike"
            user.password = "1234"
            create(user)
        }
    }

    fun create(user: User): User {
        user.id = null
        return userRepository.save(user)
    }

    fun update(user: User): User {
        if (user.id == null) throw BadRequestException()
        if (!userRepository.existsById(user.id!!)) throw NotFoundException()
        val u: User = userRepository.getOne(user.id!!)

        if (user.username != null) u.username = user.username
        if (user.password != null) u.password = user.password

        return userRepository.save(u)
    }

    fun findById(id: Long): User {
        if (!userRepository.existsById(id)) throw NotFoundException("User with id $id doesn't exist")
        return userRepository.getOne(id)
    }

    fun findByUsername(username: String): User {
        return userRepository.findByUsername(username)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun delete(userId: Long) {
        if (!userRepository.existsById(userId)) throw NotFoundException()
        userRepository.deleteById(userId)
    }

    fun whoAmI(): User {
        return userTokenService.getUser()
    }

}