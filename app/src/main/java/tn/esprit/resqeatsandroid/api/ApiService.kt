package tn.esprit.resqeatsandroid.api

import retrofit2.http.GET
import tn.esprit.resqeatsandroid.model.Restaurant

interface ApiService {
    @GET("restaurant/restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>
}


