package bbct.android.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseballCardDao {
    @Insert
    suspend fun insertBaseballCard(card: BaseballCard)

    @Update
    fun updateBaseballCard(card: BaseballCard)

    @Delete
    suspend fun deleteBaseballCards(cards: List<BaseballCard>)

    @get:Query("SELECT * FROM baseball_cards")
    val baseballCards: Flow<List<BaseballCard>>

    @Query(
        "SELECT * FROM baseball_cards " +
        "WHERE brand LIKE :brand " +
        "  AND (year = :year OR -1 = :year) " +
        "  AND number LIKE :number " +
        "  AND player_name LIKE :playerName " +
        "  AND team LIKE :team"
    )
    fun getBaseballCards(
        brand: String,
        year: Int,
        number: String,
        playerName: String,
        team: String,
    ): Flow<List<BaseballCard>>

    @Query("SELECT * FROM baseball_cards WHERE _id = :id")
    suspend fun getBaseballCard(id: Long): BaseballCard

    @get:Query("SELECT DISTINCT(brand) FROM baseball_cards")
    val brands: Flow<String>

    @get:Query("SELECT DISTINCT(player_name) FROM baseball_cards")
    val playerNames: Flow<String>

    @get:Query("SELECT DISTINCT(team) FROM baseball_cards")
    val teams: Flow<String>
}
