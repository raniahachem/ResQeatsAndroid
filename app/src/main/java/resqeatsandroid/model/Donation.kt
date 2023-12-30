package resqeatsandroid.model

data class Donation(
    val id: String,
    val title: String,
    val description: String,
    val quantity: Int,
    val date: String,
    val status: String,
    val imageUrl: String? = null

)
