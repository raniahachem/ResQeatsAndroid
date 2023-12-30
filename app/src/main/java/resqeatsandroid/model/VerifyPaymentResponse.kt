package tn.esprit.resqeatsandroid.model

data class VerifyPaymentResponse(
    val success: Boolean,
    val result: VerifyPaymentResult
)
data class VerifyPaymentResult(
    val status: String,
    val type: String,
    val details: VerifyPaymentDetails,
    val developerTrackingId: String
)

data class VerifyPaymentDetails(
    val orderNumber: String,
    val name: String,
    val phoneNumber: String,
    val email: String
)