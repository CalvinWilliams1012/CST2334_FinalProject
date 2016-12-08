package cst2335.smartenvironment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Temperature extends AppCompatActivity {

    private final static String ACTIVITY_NAME = "Temperature";
    TemperatureDatabaseHelper helper;
    SQLiteDatabase db;
    ImageButton upButton;
    ImageButton downButton;
    Button editButton;
    EditText tempText;
    int temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tempText = (EditText) findViewById(R.id.tempText);
        upButton = (ImageButton) findViewById(R.id.upButton);
        upButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setTemperature(getTemperature()+1);
                textColour();
            }
        });
        downButton = (ImageButton) findViewById(R.id.downButton);
        downButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setTemperature(getTemperature()-1);
                textColour();
            }
        });
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setTemperature(Integer.parseInt(tempText.getText().toString()));
                Snackbar.make(v, "Temperature set to " + getTemperature(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                textColour();
            }
        });

        load();
    }

    public void load() {
        helper = new TemperatureDatabaseHelper(this);
        db = helper.getWritableDatabase();

        Cursor qry = db.rawQuery("SELECT * FROM " + helper.DATABASE_NAME,new String[]{});
        int tempPlacement = qry.getColumnIndex(helper.CURRENTTEMP);

        if(qry.getCount()==0){
            db.execSQL("INSERT INTO " + helper.DATABASE_NAME + "(" + helper.CURRENTTEMP + ", " + helper.ID + ") VALUES(20,1);");
        }

        if(tempPlacement >= 0 && !qry.isClosed() && qry.getCount()!=0){
            try {
                while (qry.moveToNext()) {
                    setTemperature(qry.getInt(tempPlacement));
                }
            }finally {
                qry.close();
            }
        }

        textColour();
    }

    public void textColour(){
        if(temperature>24){
            tempText.setBackgroundColor(Color.rgb(255,0,0));
        }else if(temperature<19){
            tempText.setBackgroundColor(Color.rgb(0,0,255));
        }
    }

    private void setTemperature(int val){
        temperature = val;
        tempText.setText(temperature+"");
    }

    private int getTemperature(){
        return temperature;
    }


    @Override
    protected void onPause() {
        db.execSQL("UPDATE " + helper.DATABASE_NAME + " SET " + helper.CURRENTTEMP + "=" + temperature + " WHERE " + helper.ID + "=1;");
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
                intent = new Intent(Temperature.this,Car.class);
                startActivity(intent);
                break;
            case R.id.kitchenMenu:
                Log.i(ACTIVITY_NAME," Kitchen Selected");
                intent = new Intent(Temperature.this,Kitchen.class);
                startActivity(intent);
                break;
            case R.id.livingMenu:
                Log.i(ACTIVITY_NAME," Living Room Selected");
                intent = new Intent(Temperature.this,LivingRoom.class);
                startActivity(intent);
                break;
            case R.id.homeMenu:
                Log.i(ACTIVITY_NAME," Home Selected");
                intent = new Intent(Temperature.this,Home.class);
                startActivity(intent);
                break;
            case R.id.backMenu:
                Log.i(ACTIVITY_NAME," Back Selected");
                backButton();
                break;
            case R.id.helpMenu:
                Log.i(ACTIVITY_NAME," Help Selected");
                android.support.v7.app.AlertDialog.Builder about = new android.support.v7.app.AlertDialog.Builder(Temperature.this);
                about.setTitle("About");
                about.setMessage("Version 1.0, by Calvin Williams \n\nUse up/down buttons or type the desired temperature.");

                about.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                about.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void backButton(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Temperature.this);
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
