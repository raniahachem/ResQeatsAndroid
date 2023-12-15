package resqeatsandroid.network

import resqeatsandroid.api.DonationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitDonation {

    private const val BASE_URL = "http://192.168.134.221:7001/" // Replace with your actual backend URL

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val donationApi: DonationApi by lazy {
        retrofit.create(DonationApi::class.java)
    }
}
