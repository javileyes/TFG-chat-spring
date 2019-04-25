package es.javiergimenez.chat.security.aspect

import es.javiergimenez.chat.config.exception.UnauthorizedException
import es.javiergimenez.chat.service.UserTokenService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.regex.Pattern

@Aspect
@Order(1)
@Component
class SecurityAspect {

    var bearerRegex = Pattern.compile("Bearer (?<token>[a-zA-Z0-9-]+)")

    @Autowired lateinit var userTokenService: UserTokenService


    @Before("(@within(es.javiergimenez.chat.security.aspect.Security) || @annotation(es.javiergimenez.chat.security.aspect.Security)) && !@annotation(es.javiergimenez.chat.security.aspect.SecurityIgnore)")
    fun securityBefore(joinPoint: JoinPoint) {
        val token = getTokenFromRequest()

        if (!userTokenService.existsByTokenAndCheckDate(token)) {
            throw UnauthorizedException()
        }
    }

    fun getTokenFromRequest(): String {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val authorization = request.getHeader("Authorization") ?: throw UnauthorizedException()

        val matcher = bearerRegex.matcher(authorization)
        if (!matcher.matches()) {
            throw UnauthorizedException()
        }
        return matcher.group("token")
    }
}