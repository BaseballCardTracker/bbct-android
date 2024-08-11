package bbct.android.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baseball_cards")
data class BaseballCard(
    @PrimaryKey
    val _id: Long,
    val autographed: Boolean,
    val condition: String,
    val brand: String,
    val year: Int,
    val number: String,
    val value: Int,
    @ColumnInfo(name = "card_count")
    val quantity: Int,
    @ColumnInfo(name = "player_name")
    val playerName: String,
    val team: String,
    @ColumnInfo(name = "player_position")
    val position: String,
)
