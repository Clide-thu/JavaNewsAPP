package Backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class APPSQLHelper extends SQLiteOpenHelper {
    public static final String SQL_NAME = "APP.db";
    public static final int SQL_VERSION = 1;
    public static final String EVENTS_TABLE = "events";

    private static final String EVENTS_CREATE_SQL = "create table "
            + EVENTS_TABLE +"("
            + "id integer primary key autoincrement,"
            + "_id text,"
            + "json text,"
            + "watched integer,"
            + "unique(_id,watched)"
            + ")";

    public APPSQLHelper( Context context) {
        super(context, SQL_NAME, null, SQL_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EVENTS_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
