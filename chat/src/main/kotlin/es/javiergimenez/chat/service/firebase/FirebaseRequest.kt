package es.javiergimenez.chat.service.firebase

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FirebaseRequest(
        @SerializedName("to")
        val topic: String,
        val notification: Notification? = null,
        val data: Map<String, Any>? = null
): Serializable {


    data class Notification(
            val title: String? = null,
            val body: String? = null
    ): Serializable

}