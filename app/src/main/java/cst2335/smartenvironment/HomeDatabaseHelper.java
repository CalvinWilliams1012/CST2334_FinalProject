package cst2335.smartenvironment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Calvin on 2016-12-06.
 */

public class HomeDatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Home";
    private static final int VERSION_NUM = 4;
    public static final String NAME = "Name";
    public static final String ENABLED = "Enabled";

    public HomeDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + ENABLED + " INTEGER DEFAULT 1, " + NAME + " TEXT PRIMARY KEY);");
        Log.i("HomeDatabaseHelper", " OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM " + DATABASE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
        Log.i("HomeDatabaseHelper","Calling onUpgrade, oldVersion = " + oldVersion + " newVersion = " + newVersion);
    }
}
