package es.javiergimenez.chat.web.rest

import es.javiergimenez.chat.domain.User
import es.javiergimenez.chat.security.aspect.Security
import es.javiergimenez.chat.security.aspect.SecurityIgnore
import es.javiergimenez.chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Security
@RestController
class UserRest {

    @Autowired lateinit var userService: UserService


    @GetMapping("/api/users/whoami")
    fun getUserWhoAmI(): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.whoAmI())
    }

    @GetMapping("/api/users")
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll())
    }

    @GetMapping("/api/users/{userId}")
    fun getUser(@PathVariable userId: Long): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId))
    }

    @SecurityIgnore
    @PostMapping("/api/users/singup")
    fun postUserSingup(
            @RequestParam username: String,
            @RequestParam password: String): ResponseEntity<User> {
        val user = User()
        user.username = username
        user.password = password
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user))
    }

    @PatchMapping("/api/users")
    fun patchUser(@PathVariable userId: Long, @RequestBody user: User): ResponseEntity<User> {
        user.id = userService.whoAmI().id
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user))
    }

    @DeleteMapping("/api/users")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Nothing> {
        userService.delete(userService.whoAmI().id!!)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
    }

}