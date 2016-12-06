package cst2335.smartenvironment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Garage extends AppCompatActivity {
    private final static String ACTIVITY_NAME = "Garage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
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
