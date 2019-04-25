package es.javiergimenez.chat.model

import es.javiergimenez.chat.domain.User
import java.io.Serializable


data class LoginResponse(

        var token: String? = null,
        var user: User? = null

): Serializable