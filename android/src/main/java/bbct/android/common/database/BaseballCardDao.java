package bbct.android.common.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BaseballCardDao {
    @Insert
    void insertBaseballCard(BaseballCard card);

    @Update
    void updateBaseballCard(BaseballCard card);

    @Delete
    void deleteBaseballCards(List<BaseballCard> cards);

    @Query("SELECT * FROM baseball_cards")
    LiveData<List<BaseballCard>> getBaseballCards();

    @Query(
        "SELECT * FROM baseball_cards " +
        "WHERE brand LIKE :brand " +
        "  AND (year = :year OR -1 = :year) " +
        "  AND number LIKE :number " +
        "  AND player_name LIKE :playerName " +
        "  AND team LIKE :team"
    )
    LiveData<List<BaseballCard>> getBaseballCards(
        String brand, int year, String number, String playerName, String team
    );

    @Query("SELECT * FROM baseball_cards WHERE _id = :id")
    BaseballCard getBaseballCard(long id);

    @Query("SELECT DISTINCT(brand) FROM baseball_cards")
    LiveData<List<String>> getBrands();

    @Query("SELECT DISTINCT(player_name) FROM baseball_cards")
    LiveData<List<String>> getPlayerNames();

    @Query("SELECT DISTINCT(team) FROM baseball_cards")
    LiveData<List<String>> getTeams();
}
