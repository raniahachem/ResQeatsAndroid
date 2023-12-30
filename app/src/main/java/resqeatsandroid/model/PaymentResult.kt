package tn.esprit.resqeatsandroid.model

data class PaymentResult(
    val link: String,
    val paymentId: String,
    val developerTrackingId: String,
    val success: Boolean
)