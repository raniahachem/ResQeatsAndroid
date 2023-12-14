package tn.esprit.resqeatsandroid.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import resqeatsandroid.api.ApiService

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.19:7001/"

    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
