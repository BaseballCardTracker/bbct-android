package bbct.android.ui.details

import bbct.android.data.BaseballCard

data class DetailsState(
    var id: Long? = null,
    var autographed: Boolean = false,
    var condition: String = "",
    var brand: String = "",
    var year: String = "",
    var number: String = "",
    var value: String? = null,
    var quantity: String = "",
    var playerName: String = "",
    var team: String = "",
    var position: String = "",
) {
    constructor(card: BaseballCard) : this(
        card._id,
        card.autographed,
        card.condition,
        card.brand,
        card.year.toString(),
        card.number,
        if (card.value == null) null else (card.value / 100.0).toString(),
        card.quantity.toString(),
        card.playerName,
        card.team,
        card.position
    )

    fun toBaseballCard(): BaseballCard {
        return BaseballCard(
            _id = id,
            autographed = autographed,
            condition = condition,
            brand = brand,
            year = year.toInt(),
            number = number,
            value = if (value == null) null else (value!!.toDouble() * 100).toInt(),
            quantity = quantity.toInt(),
            playerName = playerName,
            team = team,
            position = position
        )
    }
}
