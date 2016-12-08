package cst2335.smartenvironment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private final String ACTIVITY_NAME = "Home";
    ListView mainList;
    ArrayList<String> values;
    HomeDatabaseHelper helper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        load();


        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(values.get(0).equals("Garage") && position==0){
                    Log.d(ACTIVITY_NAME,"Garage clicked");
                    Intent intent = new Intent(Home.this,Garage.class);
                    startActivity(intent);
                }else if(values.get(1).equals("Temperature")&& position==1){
                    Log.d(ACTIVITY_NAME,"Temp clicked");
                    Intent intent = new Intent(Home.this,Temperature.class);
                    startActivity(intent);
                }else if(values.get(2).equals("Weather") && position==2){
                    Log.d(ACTIVITY_NAME,"Weather clicked");
                    Intent intent = new Intent(Home.this,Weather.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void load(){

        mainList = (ListView) findViewById(R.id.homeListView);
        values = new ArrayList<>();

        helper = new HomeDatabaseHelper(this);
        db = helper.getWritableDatabase();

        ItemAdapter myAdapter = new ItemAdapter(this);

        Cursor qry = db.rawQuery("SELECT * FROM " + helper.DATABASE_NAME,new String[]{});

        if(qry.getCount()==0){
            db.execSQL("INSERT INTO "+ helper.DATABASE_NAME + "(" + helper.ENABLED + ", " + helper.NAME + ") VALUES('1','Garage')");
            db.execSQL("INSERT INTO "+ helper.DATABASE_NAME + "(" + helper.ENABLED + ", " + helper.NAME + ") VALUES('1','Temperature')");
            db.execSQL("INSERT INTO "+ helper.DATABASE_NAME + "(" + helper.ENABLED + ", " + helper.NAME + ") VALUES('1','Weather')");
            db.execSQL("INSERT INTO "+ helper.DATABASE_NAME + "(" + helper.ENABLED + ", " + helper.NAME + ") VALUES('0','Robot Vacuum')");
            db.execSQL("INSERT INTO "+ helper.DATABASE_NAME + "(" + helper.ENABLED + ", " + helper.NAME + ") VALUES('0','Sound System')");
        }

        int dbName = qry.getColumnIndex(helper.NAME);
        int dbEnabled = qry.getColumnIndex(helper.ENABLED);
        Log.i(ACTIVITY_NAME,"Col count: " + qry.getColumnCount());
        if(dbName >= 0 && dbEnabled >= 0 && !qry.isClosed() && qry.getCount()!=0){
            try {
                while (qry.moveToNext()) {
                    if (qry.getInt(dbEnabled) != 0) {
                        values.add(qry.getString(dbName));
                    }
                }
            }finally {
                qry.close();
            }
        }

        mainList.setAdapter(myAdapter);
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
                intent = new Intent(Home.this,Car.class);
                startActivity(intent);
                break;
            case R.id.kitchenMenu:
                Log.i(ACTIVITY_NAME," Kitchen Selected");
                intent = new Intent(Home.this,Kitchen.class);
                startActivity(intent);
                break;
            case R.id.livingMenu:
                Log.i(ACTIVITY_NAME," Living Room Selected");
                intent = new Intent(Home.this,LivingRoom.class);
                startActivity(intent);
                break;
            case R.id.homeMenu:
                android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(Home.this);
                builder2.setTitle("Home Interface"); //Set message to This is the current interface that you are in.
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.home_dialog, null);
                builder2.setView(view);
                final TextView dialogText = (TextView) view.findViewById(R.id.customHomeDialog);

                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                android.support.v7.app.AlertDialog build = builder2.create();
                build.show();

                Log.i(ACTIVITY_NAME," Home Selected");
                break;
            case R.id.backMenu:
                Log.i(ACTIVITY_NAME," Back Selected");
                backButton();
                break;
            case R.id.helpMenu:
                Log.i(ACTIVITY_NAME," Help Selected");
                android.support.v7.app.AlertDialog.Builder about = new android.support.v7.app.AlertDialog.Builder(Home.this);
                about.setTitle("About");
                about.setMessage("Version 1.0, by Calvin Williams \n\nSelect items from the list to open controls for that item.");

                about.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                about.show();
                break;
            case R.id.addMenu:
                Log.i(ACTIVITY_NAME," Add Selected");
                intent = new Intent(Home.this,Add_Items.class);
                startActivity(intent);
                break;
            case R.id.removeMenu:
                Log.i(ACTIVITY_NAME,"Remove Selected");
                intent = new Intent(Home.this,Remove_Items.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void backButton(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
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

    private class ItemAdapter extends ArrayAdapter<String>{

        public ItemAdapter(Context c){
            super(c,0);
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public String getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Home.this.getLayoutInflater();
            View result;
            TextView message;
            String val = getItem(position);
            if(val.equals("Garage")){
                result = inflater.inflate(R.layout.garage_list_item, null);
                message = (TextView)result.findViewById(R.id.itemTView);
                message.setText(getItem(position));
            }else if(val.equals("Temperature")){
                result = inflater.inflate(R.layout.temperature_list_item, null);
                message = (TextView)result.findViewById(R.id.itemTView);
                message.setText(getItem(position));
            }else if(val.equals("Weather")) {
                result = inflater.inflate(R.layout.weather_list_item, null);
                message = (TextView) result.findViewById(R.id.itemTView);
                message.setText(getItem(position));
            }else if(val.equals("Robot Vacuum")) {
                    result = inflater.inflate(R.layout.vacuum_list_item, null);
                    message = (TextView) result.findViewById(R.id.itemTView);
                    message.setText(getItem(position));
            }else if(val.equals("Sound System")){
                    result = inflater.inflate(R.layout.sound_list_item, null);
                    message = (TextView) result.findViewById(R.id.itemTView);
                    message.setText(getItem(position));
            }else{
                result = inflater.inflate(R.layout.home_list_item, null);
                message = (TextView)result.findViewById(R.id.itemTView);
                message.setText(getItem(position));
            }

            return result;
        }
    }
}
