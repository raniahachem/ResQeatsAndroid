package tn.esprit.resqeatsandroid.model

data class Order (
    val status: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val client: String,
    val _id: String
)