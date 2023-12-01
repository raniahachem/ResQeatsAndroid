package tn.esprit.resqeatsandroid.api

import retrofit2.http.GET
import retrofit2.http.Path
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.model.Restaurant

interface ApiService {
    @GET("restaurant/restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>

    @GET("product/products")
    suspend fun getAllProducts(): List<Product>

    @GET("product/{restaurantId}/products")
    suspend fun getAllProductsByRestaurantId(@Path("restaurantId") restaurantId: String): List<Product>
}


