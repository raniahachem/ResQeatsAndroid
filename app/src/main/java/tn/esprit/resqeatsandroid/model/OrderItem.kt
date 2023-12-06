package tn.esprit.resqeatsandroid.model

data class OrderItem (
    val product: Product,
    val quantity: Int,
    val _id: String
)