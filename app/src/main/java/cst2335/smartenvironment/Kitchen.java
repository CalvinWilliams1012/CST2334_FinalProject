package cst2335.smartenvironment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Kitchen extends AppCompatActivity {

    private final String ACTIVITY_NAME = "Kitchen";
    Button okay;
    TextView kitchenText;
    ImageView kitchenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Sets the text of the current activity text*/
        kitchenText = (TextView) findViewById(R.id.customKitchenDialog);
        Resources res = getResources();
        String text = String.format(res.getString(R.string.Current),ACTIVITY_NAME);
        kitchenText.setText(text);

        /* Sets the okay button and whenever it is pressed the current activity dialog is dismissed*/
        okay =(Button) findViewById(R.id.okButtonKitchen);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kitchenText.setVisibility(View.INVISIBLE);
                kitchenView = (ImageView) findViewById(R.id.kitchenImg);
                kitchenView.setVisibility(View.INVISIBLE);
                okay.setVisibility(View.INVISIBLE);
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
                intent = new Intent(Kitchen.this,Car.class);
                startActivity(intent);
                break;
            case R.id.kitchenMenu:
                Log.i(ACTIVITY_NAME," Kitchen Selected");
                intent = new Intent(Kitchen.this,Kitchen.class);
                startActivity(intent);

                /*Sets the current activity dialog to show*/
                okay.setVisibility(View.VISIBLE);
                kitchenText.setVisibility(View.VISIBLE);
                kitchenView.setVisibility(View.VISIBLE);
                break;
            case R.id.livingMenu:
                Log.i(ACTIVITY_NAME," Living Room Selected");
                intent = new Intent(Kitchen.this,LivingRoom.class);
                startActivity(intent);
                break;
            case R.id.homeMenu:
                Log.i(ACTIVITY_NAME," Home Selected");
                intent = new Intent(Kitchen.this,Home.class);
                startActivity(intent);
                break;
            case R.id.exitMenu:
                Log.i(ACTIVITY_NAME," Exit Selected");
                exitButton();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void exitButton(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Kitchen.this);
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
