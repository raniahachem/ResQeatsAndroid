package tn.esprit.resqeatsandroid.model

sealed class HomeItem {
    data class RestaurantItem(val restaurant: Restaurant) : HomeItem()
    data class ProductItem(val product: Product) : HomeItem()
}

