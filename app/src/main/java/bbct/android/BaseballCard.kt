package bbct.android

data class BaseballCard(
    val autographed: Boolean,
    val condition: String,
    val brand: String,
    val year: Int,
    val number: String,
    val value: Int,
    val quantity: Int,
    val playerName: String,
    val team: String,
    val position: String,
)
