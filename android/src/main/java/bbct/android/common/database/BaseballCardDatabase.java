package bbct.android.common.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import bbct.android.common.provider.BaseballCardSQLHelper;

@Database(version = BaseballCardSQLHelper.ROOM_SCHEMA, entities = {BaseballCard.class})
public abstract class BaseballCardDatabase extends RoomDatabase {
    private static BaseballCardDatabase instance = null;
    private static final String DATABASE_NAME = "bbct.db";

    private static final Migration MIGRATION_4_5 = new Migration(
            BaseballCardSQLHelper.AUTO_AND_CONDITION_SCHEMA,
            BaseballCardSQLHelper.ROOM_SCHEMA) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    public static BaseballCardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BaseballCardDatabase.class,
                    DATABASE_NAME)
                    .addMigrations(MIGRATION_4_5)
                    .build();
        }

        return instance;
    }

    abstract public BaseballCardDao getBaseballCardDao();
}
