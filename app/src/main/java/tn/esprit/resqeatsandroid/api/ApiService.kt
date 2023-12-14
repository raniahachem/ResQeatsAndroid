package tn.esprit.resqeatsandroid.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.resqeatsandroid.model.EmailRequest
import tn.esprit.resqeatsandroid.model.EmailResponse
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.model.Restaurant
import tn.esprit.resqeatsandroid.model.Order
import tn.esprit.resqeatsandroid.model.PaymentRequest
import tn.esprit.resqeatsandroid.model.PaymentResponse
import tn.esprit.resqeatsandroid.model.VerifyPaymentResponse

interface ApiService {
    @GET("restaurant/restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>

    @GET("product/products")
    suspend fun getAllProducts(): List<Product>

    @GET("product/{restaurantId}/products")
    suspend fun getAllProductsByRestaurantId(@Path("restaurantId") restaurantId: String): List<Product>

    @POST("product/products")
    suspend fun addProduct(@Body product: Product): Response<Void>

    @POST("/order/orders")
    suspend fun createOrder(@Body order: Order): Response<Order>
    @POST("/payments/payment")
    fun initiatePayment(@Body paymentRequest: PaymentRequest): Call<PaymentResponse>

    @GET("/payments/verify/{id}")
    fun verifyPayment(@Path("id") paymentId: String): Call<VerifyPaymentResponse>

    @GET("/user/users/{id}/email")
    suspend fun getEmailById(@Path("id") userId: String): Response<EmailResponse>

    @POST("/email/send-email")
    suspend fun sendEmail(@Body emailRequest: EmailRequest): Response<Void>
}


