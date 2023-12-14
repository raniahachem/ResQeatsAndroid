package tn.esprit.resqeatsandroid.model

data class EmailResponse(
    val email: String
)

data class EmailRequest(
    val to: String,
    val subject: String,
    val text: String
)
