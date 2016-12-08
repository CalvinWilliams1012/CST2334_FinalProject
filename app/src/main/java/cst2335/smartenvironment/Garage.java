package cst2335.smartenvironment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Garage extends AppCompatActivity {
    private final static String ACTIVITY_NAME = "Garage";
    boolean garageDoor;
    boolean lights;
    GarageDatabaseHelper helper;
    SQLiteDatabase db;
    Button gButton;
    Button lButton;
    ImageView garageImg;
    ImageView lightImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gButton = (Button) findViewById(R.id.garageButton);
        lButton = (Button) findViewById(R.id.lightButton);
        garageImg = (ImageView) findViewById(R.id.garageImage);
        lightImg = (ImageView) findViewById(R.id.lightImage);

        gButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(garageDoor){
                    garageImg.setImageResource(R.drawable.garage_closed);
                    garageDoor = false;
                }else if(!garageDoor){
                    garageImg.setImageResource(R.drawable.garage_open);
                    lightImg.setImageResource(R.drawable.lighton);
                    garageDoor = true;
                    lights = true;
                }
            }
        });

        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lights){
                    lightImg.setImageResource(R.drawable.lightoff);
                    lights = false;
                }else if(!lights){
                    lightImg.setImageResource(R.drawable.lighton);
                    lights = true;
                }
            }
        });
        load();
    }

    public void load(){
        helper = new GarageDatabaseHelper(this);
        db = helper.getWritableDatabase();

        Cursor qry = db.rawQuery("SELECT * FROM " + helper.DATABASE_NAME,new String[]{});

        int garageStatus = qry.getColumnIndex(helper.DOOR);
        int lightStatus = qry.getColumnIndex(helper.LIGHT);

        if(qry.getCount()==0){
            db.execSQL("INSERT INTO " + helper.DATABASE_NAME + "(" + helper.DOOR + ", " + helper.LIGHT + ", " + helper.ID + ") VALUES(1,1,1);");
        }

        if(garageStatus >= 0 && lightStatus >= 0 && !qry.isClosed() && qry.getCount()!=0){
            try {
                while (qry.moveToNext()) {
                    if(qry.getInt(garageStatus)==0){
                        garageDoor  = false;
                    }else if(qry.getInt(garageStatus)==1){
                        garageDoor = true;
                    }
                    if(qry.getInt(lightStatus)==0){
                        lights = false;
                    }else if(qry.getInt(lightStatus)==1){
                        lights = true;
                    }
                }
            }finally {
                qry.close();
            }
        }
        if(garageDoor){
            garageImg.setImageResource(R.drawable.garage_open);
        }else if(!garageDoor){
            garageImg.setImageResource(R.drawable.garage_closed);
        }

        if(lights){
            lightImg.setImageResource(R.drawable.lighton);
        }else if(!lights){
            lightImg.setImageResource(R.drawable.lightoff);
        }
    }

    @Override
    protected void onPause() {
        if(garageDoor && lights){
            db.execSQL("UPDATE " + helper.DATABASE_NAME + " SET " + helper.DOOR + "=1, " + helper.LIGHT + "=1 WHERE " + helper.ID + "=1;");
        }else if(!garageDoor && !lights){
            db.execSQL("UPDATE " + helper.DATABASE_NAME + " SET " + helper.DOOR + "=0, " + helper.LIGHT + "=0 WHERE " + helper.ID + "=1;");
        }else if(!garageDoor && lights){
            db.execSQL("UPDATE " + helper.DATABASE_NAME + " SET " + helper.DOOR + "=0, " + helper.LIGHT + "=1 WHERE " + helper.ID + "=1;");
        }else if(garageDoor && !lights){
            db.execSQL("UPDATE " + helper.DATABASE_NAME + " SET " + helper.DOOR + "=1, " + helper.LIGHT + "=0 WHERE " + helper.ID + "=1;");
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.carMenu:
                Log.i(ACTIVITY_NAME," Car Selected");
                intent = new Intent(Garage.this,Car.class);
                startActivity(intent);
                break;
            case R.id.kitchenMenu:
                Log.i(ACTIVITY_NAME," Kitchen Selected");
                intent = new Intent(Garage.this,Kitchen.class);
                startActivity(intent);
                break;
            case R.id.livingMenu:
                Log.i(ACTIVITY_NAME," Living Room Selected");
                intent = new Intent(Garage.this,LivingRoom.class);
                startActivity(intent);
                break;
            case R.id.homeMenu:
                Log.i(ACTIVITY_NAME," Home Selected");
                intent = new Intent(Garage.this,Home.class);
                startActivity(intent);
                break;
            case R.id.backMenu:
                Log.i(ACTIVITY_NAME," Back Selected");
                backButton();
                break;
            case R.id.helpMenu:
                Log.i(ACTIVITY_NAME," Help Selected");
                android.support.v7.app.AlertDialog.Builder about = new android.support.v7.app.AlertDialog.Builder(Garage.this);
                about.setTitle("About");
                about.setMessage("Version 1.0, by Calvin Williams \n\nUse the buttons to switch the objects state.");

                about.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                about.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backButton(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Garage.this);
        builder.setTitle("Do you want to go back?");

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });
        builder.show();
    }


}
