package es.javiergimenez.chat.service.firebase

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import es.javiergimenez.chat.domain.Message
import es.javiergimenez.chat.domain.User
import es.javiergimenez.chat.service.ChatService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.annotation.PostConstruct


@Component
class FirebaseRetrofit {

    private val BASE_URL = "https://fcm.googleapis.com"
    @Value("\${firebase.secret}") lateinit var secret: String
    lateinit var firebaseApi: FirebaseApi

    enum class Type {
        MESSAGE
    }


    @PostConstruct
    fun init() {
        val builder = Retrofit.Builder().baseUrl(BASE_URL)
        builder.client(createHttpClient())
        val retrofit = builder
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build()

        firebaseApi = retrofit.create<FirebaseApi>(FirebaseApi::class.java)
    }

    private fun createGson(): Gson {
        return GsonBuilder().create()
    }

    private fun createHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(loggingInterceptor)
        return httpClient.build()
    }



    fun send(users: List<User>, message: Message) {
        users.stream().forEach {

            val data = mapOf(
                    "type" to Type.MESSAGE.toString(),
                    "id" to message.id!!,
                    "body" to message.body!!,
                    "date" to message.date.toString(),
                    "userId" to message.user!!.id!!,
                    "username" to message.user!!.username!!,
                    "chatId" to message.chat!!.id!!,
                    "title" to ChatService.getTitleFromChat(it.id!!, message.chat!!),
                    "channelId" to message.chat!!.channelId!!
            )

            send("/topics/user_${it.id}", message.user!!.username!!, message.body!!, data)
        }
    }

    @Async
    fun send(topic: String, title: String, body: String, data: Map<String, Any>) {
        val firebaseRequest = FirebaseRequest(topic, FirebaseRequest.Notification(title, body), data)
        firebaseApi.postSend("key=$secret", firebaseRequest).execute()
    }

}