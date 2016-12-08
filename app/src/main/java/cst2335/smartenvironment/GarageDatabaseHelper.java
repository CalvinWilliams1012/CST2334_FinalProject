package cst2335.smartenvironment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Calvin on 2016-12-07.
 */

public class GarageDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Garage";
    private static final int VERSION_NUM = 2;
    public static final String DOOR = "Door";
    public static final String LIGHT = "Lights";
    public static final String ID = "id";

    public GarageDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + ID + " INTEGER PRIMARY KEY," + DOOR + " INTEGER," + LIGHT + " INTEGER);");
        Log.i("GarageDatabaseHelper", " OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM " + DATABASE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
        Log.i("GarageDatabaseHelper","Calling onUpgrade, oldVersion = " + oldVersion + " newVersion = " + newVersion);
    }
}
