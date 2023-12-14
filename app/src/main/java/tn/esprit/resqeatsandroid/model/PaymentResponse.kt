package tn.esprit.resqeatsandroid.model

data class PaymentResponse(
    val result: PaymentResult,
    val code: Int,
    val name: String,
    val version: String
)