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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Add_Items extends AppCompatActivity {
    ListView mainList;
    ArrayList<String> values;
    HomeDatabaseHelper helper;
    SQLiteDatabase db;

    private final static String ACTIVITY_NAME = "Add_Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainList = (ListView) findViewById(R.id.addListView);
        values = new ArrayList<>();

        helper = new HomeDatabaseHelper(this);
        db = helper.getWritableDatabase();

        Add_Items.ItemAdapter myAdapter = new Add_Items.ItemAdapter(this);

        Cursor qry = db.rawQuery("SELECT * FROM " + helper.DATABASE_NAME,new String[]{});

        int dbName = qry.getColumnIndex(helper.NAME);
        int dbEnabled = qry.getColumnIndex(helper.ENABLED);
        Log.i(ACTIVITY_NAME,"Col count: " + qry.getColumnCount());
        if(dbName >= 0 && dbEnabled >= 0 && !qry.isClosed() && qry.getCount()!=0){
            try {
                while (qry.moveToNext()) {
                    if (qry.getInt(dbEnabled)== 0) {
                        values.add(qry.getString(dbName));
                    }
                }
            }finally {
                qry.close();
            }
        }

        mainList.setAdapter(myAdapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(values.get(0).equals("Robot Vacuum") && position==0){
                    Log.d(ACTIVITY_NAME,"Vacuum clicked");
                    addItem("Robot Vacuum");
                }else if(values.get(1).equals("Sound System")&& position==1){
                    Log.d(ACTIVITY_NAME,"Sound clicked");
                    addItem("Sound System");
                }
            }
        });
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
                intent = new Intent(Add_Items.this,Car.class);
                startActivity(intent);
                break;
            case R.id.kitchenMenu:
                Log.i(ACTIVITY_NAME," Kitchen Selected");
                intent = new Intent(Add_Items.this,Kitchen.class);
                startActivity(intent);
                break;
            case R.id.livingMenu:
                Log.i(ACTIVITY_NAME," Living Room Selected");
                intent = new Intent(Add_Items.this,LivingRoom.class);
                startActivity(intent);
                break;
            case R.id.homeMenu:
                intent = new Intent(Add_Items.this,Home.class);
                startActivity(intent);
                Log.i(ACTIVITY_NAME," Home Selected");
                break;
            case R.id.backMenu:
                Log.i(ACTIVITY_NAME," Back Selected");
                backButton();
                break;
            case R.id.helpMenu:
                Log.i(ACTIVITY_NAME," Help Selected");
                android.support.v7.app.AlertDialog.Builder about = new android.support.v7.app.AlertDialog.Builder(Add_Items.this);
                about.setTitle("About");
                about.setMessage("Version 1.0, by Calvin Williams \n\nSelect items from the list to open controls for that item.");

                about.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                about.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void backButton(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Add_Items.this);
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

    private class ItemAdapter extends ArrayAdapter<String> {

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
            LayoutInflater inflater = Add_Items.this.getLayoutInflater();
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

    public void addItem(final String name){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Add_Items.this);
        builder.setTitle("Do you want to add a " + name +"?");

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                db.execSQL("UPDATE " + helper.DATABASE_NAME + " SET " + helper.ENABLED + "=1 WHERE " + helper.NAME + "='" + name + "';");
            }
        });
        builder.show();
    }
}
