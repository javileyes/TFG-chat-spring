package es.javiergimenez.chat.web.rest

import es.javiergimenez.chat.service.UserTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserTokenRest {

    @Autowired lateinit var userTokenService: UserTokenService


    @PostMapping("/api/login")
    fun postLogin(@RequestParam username: String, @RequestParam password: String): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userTokenService.login(username, password))
    }

}