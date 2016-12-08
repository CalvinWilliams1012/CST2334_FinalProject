package cst2335.smartenvironment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Calvin on 2016-12-07.
 */

public class TemperatureDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Temperature";
    private static final int VERSION_NUM = 3;
    public static final String CURRENTTEMP = "CurrentTemperature";
    public static final String ID = "id";

    public TemperatureDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " + CURRENTTEMP + " INTEGER);");
        Log.i("TempDatabaseHelper", " OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM " + DATABASE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
        Log.i("TempDatabaseHelper","Calling onUpgrade, oldVersion = " + oldVersion + " newVersion = " + newVersion);
    }
}
