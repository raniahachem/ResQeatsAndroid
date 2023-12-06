package tn.esprit.resqeatsandroid.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.model.Restaurant
import tn.esprit.resqeatsandroid.model.Order

interface ApiService {
    @GET("restaurant/restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>

    @GET("product/products")
    suspend fun getAllProducts(): List<Product>

    @GET("product/{restaurantId}/products")
    suspend fun getAllProductsByRestaurantId(@Path("restaurantId") restaurantId: String): List<Product>

    @POST("product/products")
    suspend fun addProduct(@Body product: Product): Response<Void>

    @POST("/orders")
    suspend fun createOrder(@Body order: Order): Response<Order>
}


