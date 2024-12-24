package bbct.android.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

const val ORIGINAL_SCHEMA = 1
const val BAD_TEAM_SCHEMA = 2
const val TEAM_SCHEMA = 3
const val AUTO_AND_CONDITION_SCHEMA = 4
const val ROOM_SCHEMA = 5
const val ALPHA_NUMERIC_SCHEMA = 6
const val NOT_NULL_SCHEMA = 7

@Database(
    version = NOT_NULL_SCHEMA,
    entities = [BaseballCard::class],
    autoMigrations = [
        AutoMigration(
            from = ALPHA_NUMERIC_SCHEMA,
            to = NOT_NULL_SCHEMA
        )
    ]
)
abstract class BaseballCardDatabase : RoomDatabase() {
    abstract val baseballCardDao: BaseballCardDao

    companion object {
        internal const val DATABASE_NAME = "bbct.db"
        internal const val TEST_DATABASE_NAME = "bbct_test.db"

        private val MIGRATION_4_5: Migration = object : Migration(
            AUTO_AND_CONDITION_SCHEMA,
            ROOM_SCHEMA
        ) {
            override fun migrate(db: SupportSQLiteDatabase) {
            }
        }
        private val MIGRATION_5_6: Migration = object : Migration(
            ROOM_SCHEMA,
            ALPHA_NUMERIC_SCHEMA
        ) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val temp_table_name = BaseballCardContract.TABLE_NAME + "_new"
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                    + temp_table_name + "("
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
                    + BaseballCardContract.NUMBER_COL_NAME + "))"
                )
                db.execSQL(
                    "INSERT INTO " + temp_table_name + " ("
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
                    + " FROM " + BaseballCardContract.TABLE_NAME
                )

                db.execSQL("DROP TABLE " + BaseballCardContract.TABLE_NAME)
                db.execSQL(
                    "ALTER TABLE " + temp_table_name + " RENAME TO " + BaseballCardContract.TABLE_NAME
                )
            }
        }
        private var instances: MutableMap<String, BaseballCardDatabase> = HashMap()

        fun getInstance(
            context: Context,
            dbName: String,
        ): BaseballCardDatabase {
            var instance = instances[dbName]
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    BaseballCardDatabase::class.java,
                    dbName
                )
                    .addMigrations(
                        MIGRATION_4_5,
                        MIGRATION_5_6
                    )
                    .build()
                instances[dbName] = instance
            }

            return instance
        }
    }
}
