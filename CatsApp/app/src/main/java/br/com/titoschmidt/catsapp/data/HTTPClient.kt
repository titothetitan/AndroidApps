package br.com.titoschmidt.catsapp.data

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
 * 15/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
object HTTPClient {
    //https://api.imgur.com/3/gallery/search/?q=cats
    private const val BASE_URL = "https://api.imgur.com/"

    const val CLIENT_ID = "Client-ID 1ceddedc03a5d71"

    const val CLIENT_SECRET = "63775118a9f912fd91ed99574becf3b375d9eeca"

    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient())
            .build()
    }

    private fun httpClient() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                     val newRequest = chain.request().newBuilder()
                    //.header("Authorization", Credentials.basic(CLIENT_ID, CLIENT_SECRET))
                    .header("Authorization", CLIENT_ID)
                    .build()
                  chain.proceed(newRequest) }
            .build()
    }
}