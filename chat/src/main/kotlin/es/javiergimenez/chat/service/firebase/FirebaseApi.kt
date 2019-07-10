package es.javiergimenez.chat.service.firebase

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface FirebaseApi {


    @POST("/fcm/send")
    fun postSend(
            @Header("Authorization") secretKey: String,
            @Body firebaseRequest: FirebaseRequest
    ): Call<Void>

}