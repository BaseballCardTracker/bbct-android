package bbct.android.common.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import bbct.android.common.provider.BaseballCardSQLHelper;

@Database(version = BaseballCardSQLHelper.ROOM_SCHEMA, entities = {BaseballCard.class})
public abstract class BaseballCardDatabase extends RoomDatabase {
    private static BaseballCardDatabase instance = null;
    private static final String DATABASE_NAME = "bbct.db";

    public static BaseballCardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BaseballCardDatabase.class,
                    DATABASE_NAME)
                    .build();
        }

        return instance;
    }

    abstract public BaseballCardDao getBaseballCardDao();
}
