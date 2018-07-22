package bbct.android.common.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import bbct.android.common.provider.BaseballCardSQLHelper;

@Database(version = BaseballCardSQLHelper.ROOM_SCHEMA, entities = {BaseballCard.class})
public abstract class BaseballCardDatabase extends RoomDatabase {
    abstract public BaseballCardDao getBaseballCardDao();
}
