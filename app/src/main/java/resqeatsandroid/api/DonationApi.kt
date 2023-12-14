package resqeatsandroid.api

import resqeatsandroid.model.Donation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DonationApi {

    @GET("donation/donations")
    fun getAllDonations(): Call<List<Donation>>

    @POST("donation/donations")
    fun createDonation(@Body donation: Donation): Call<Donation>

    @PUT("donation/{id}")
    fun updateDonation(@Path("id") id: String, @Body donation: Donation): Call<Donation>

    @DELETE("donation/{id}")
    fun deleteDonation(@Path("id") id: String): Call<Void>

    @GET("donation/{id}")
    fun getDonationById(@Path("id") id: String): Call<Donation>
}
