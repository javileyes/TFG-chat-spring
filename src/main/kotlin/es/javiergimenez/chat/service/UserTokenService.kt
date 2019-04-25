package es.javiergimenez.chat.service

import es.javiergimenez.chat.config.exception.UnauthorizedException
import es.javiergimenez.chat.domain.User
import es.javiergimenez.chat.domain.UserToken
import es.javiergimenez.chat.model.LoginResponse
import es.javiergimenez.chat.repository.UserTokenRepository
import es.javiergimenez.chat.security.aspect.SecurityAspect
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class UserTokenService {

    companion object {
        const val TOKEN_VALIDITY_SECONDS = 60 * 60 * 12
    }

    @Autowired lateinit var userTokenRepository: UserTokenRepository
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var securityAspect: SecurityAspect


    fun login(username: String, password: String): LoginResponse {
        if (!userService.existsByUsername(username)) {
            throw UnauthorizedException()
        }

        val user = userService.findByUsername(username)
        if (user.password.equals(password)) {

            val userToken: UserToken

            if (userTokenRepository.existsByUser_Username(username)) {
                userToken = userTokenRepository.findByUser_Username(username)
            } else {
                userToken = UserToken()
                userToken.token = UUID.randomUUID().toString()
                userToken.user = user
            }

            userToken.date = DateTime.now()
            userTokenRepository.save(userToken)

            return LoginResponse(userToken.token, user)
        } else {
            throw UnauthorizedException()
        }

    }

    fun existsByTokenAndCheckDate(token: String): Boolean {
        if (!userTokenRepository.existsByToken(token)) {
            return false
        }

        val userToken = userTokenRepository.findByToken(token)
        if (userToken.date == null || DateTime.now().isAfter(userToken.date!!.plusSeconds(TOKEN_VALIDITY_SECONDS))) {
            userTokenRepository.delete(userToken)
            return false
        }
        return true
    }

    fun getUser(): User {
        val token = securityAspect.getTokenFromRequest()
        return userTokenRepository.findByToken(token).user!!
    }

}