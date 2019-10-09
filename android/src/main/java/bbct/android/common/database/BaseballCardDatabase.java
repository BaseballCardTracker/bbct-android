package bbct.android.common.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardSQLHelper;

@Database(version = BaseballCardSQLHelper.UPDATED_SCHEMA, entities = {BaseballCard.class})
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

    private static final Migration MIGRATION_5_6 = new Migration(
            BaseballCardSQLHelper.ROOM_SCHEMA, BaseballCardSQLHelper.UPDATED_SCHEMA) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS "
                    + BaseballCardContract.TABLE_NAME + "_new" + "("
                    + BaseballCardContract.ID_COL_NAME
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + BaseballCardContract.BRAND_COL_NAME + " TEXT, "
                    + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                    + BaseballCardContract.NUMBER_COL_NAME + " TEXT, "
                    + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                    + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                    + BaseballCardContract.PLAYER_NAME_COL_NAME + " TEXT, "
                    + BaseballCardContract.TEAM_COL_NAME + " TEXT, "
                    + BaseballCardContract.PLAYER_POSITION_COL_NAME + " TEXT,"
                    + BaseballCardContract.AUTOGRAPHED_COL_NAME + " INTEGER,"
                    + BaseballCardContract.CONDITION_COL_NAME + " TEXT,"
                    + "UNIQUE (" + BaseballCardContract.BRAND_COL_NAME + ", "
                    + BaseballCardContract.YEAR_COL_NAME + ", "
                    + BaseballCardContract.NUMBER_COL_NAME + "))");

            database.execSQL(
                    "INSERT INTO " + BaseballCardContract.TABLE_NAME + "_new ("
                            + BaseballCardContract.ID_COL_NAME + ", "
                            + BaseballCardContract.BRAND_COL_NAME + ", "
                            + BaseballCardContract.YEAR_COL_NAME + ", "
                            + BaseballCardContract.NUMBER_COL_NAME + ", "
                            + BaseballCardContract.VALUE_COL_NAME + ", "
                            + BaseballCardContract.COUNT_COL_NAME + ", "
                            + BaseballCardContract.PLAYER_NAME_COL_NAME + ", "
                            + BaseballCardContract.TEAM_COL_NAME + ", "
                            + BaseballCardContract.PLAYER_POSITION_COL_NAME + ", "
                            + BaseballCardContract.AUTOGRAPHED_COL_NAME + ", "
                            + BaseballCardContract.CONDITION_COL_NAME
                            + ") SELECT "
                            + BaseballCardContract.ID_COL_NAME + ", "
                            + BaseballCardContract.BRAND_COL_NAME + ", "
                            + BaseballCardContract.YEAR_COL_NAME + ", "
                            + BaseballCardContract.NUMBER_COL_NAME + ", "
                            + BaseballCardContract.VALUE_COL_NAME + ", "
                            + BaseballCardContract.COUNT_COL_NAME + ", "
                            + BaseballCardContract.PLAYER_NAME_COL_NAME + ", "
                            + BaseballCardContract.TEAM_COL_NAME + ", "
                            + BaseballCardContract.PLAYER_POSITION_COL_NAME + ", "
                            + BaseballCardContract.AUTOGRAPHED_COL_NAME + ", "
                            + BaseballCardContract.CONDITION_COL_NAME
                            + " FROM " + BaseballCardContract.TABLE_NAME);

            database.execSQL("DROP TABLE " + BaseballCardContract.TABLE_NAME);
            database.execSQL("ALTER TABLE " + BaseballCardContract.TABLE_NAME + "_new RENAME TO "
                    + BaseballCardContract.TABLE_NAME);
        }
    };

    public static BaseballCardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BaseballCardDatabase.class,
                    DATABASE_NAME)
                    .addMigrations(MIGRATION_4_5, MIGRATION_5_6)
                    .build();
        }

        return instance;
    }

    abstract public BaseballCardDao getBaseballCardDao();
}
