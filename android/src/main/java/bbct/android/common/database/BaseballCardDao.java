package bbct.android.common.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
        "  AND year = :year " +
        "  AND number LIKE :number " +
        "  AND player_name LIKE :playerName " +
        "  AND team LIKE :team"
    )
    LiveData<List<BaseballCard>> getBaseballCards(
        String brand, int year, String number, String playerName, String team
    );

    @Query(
        "SELECT * FROM baseball_cards " +
        "WHERE brand LIKE :brand " +
        "  AND number LIKE :number " +
        "  AND player_name LIKE :playerName " +
        "  AND team LIKE :team"
    )
    LiveData<List<BaseballCard>> getBaseballCards(
        String brand, String number, String playerName, String team
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
