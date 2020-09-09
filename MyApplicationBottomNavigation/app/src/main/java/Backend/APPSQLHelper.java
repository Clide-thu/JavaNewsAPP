package Backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class APPSQLHelper extends SQLiteOpenHelper {
    public static final String SQL_NAME = "APPNet.db";
    public static final int SQL_VERSION = 1;
    public static final String CACHE_TABLE = "cache";
    public static final String RECORD_TABLE = "record";

    private static final String CACHE_CREATE_SQL = "create table "
            + CACHE_TABLE +"("
            + "id integer primary key autoincrement,"
            + "_id text,"
            + "type text,"
            + "json text,"
            + "unique(_id)"
            + ")";

    private static final String RECORD_CREATE_SQL = "create table "
            + RECORD_TABLE +"("
            + "id integer primary key autoincrement,"
            + "_id text,"
            + "type text,"
            + "json text,"
            + "unique(_id)"
            + ")";

    public APPSQLHelper( Context context) {
        super(context, SQL_NAME, null, SQL_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CACHE_CREATE_SQL);
        db.execSQL(RECORD_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
