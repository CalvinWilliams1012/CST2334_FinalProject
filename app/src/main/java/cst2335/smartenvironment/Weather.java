package cst2335.smartenvironment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather extends AppCompatActivity {

    private final static String ACTIVITY_NAME = "Weather";
    ProgressBar pBar;
    Context ctx;
    TextView currentG;
    ImageView wtImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx = getApplicationContext();
        pBar = (ProgressBar) findViewById(R.id.weatherPBar);
        pBar.setVisibility(View.VISIBLE);
        currentG = (TextView) findViewById(R.id.currentTemp);
        wtImage = (ImageView) findViewById(R.id.weatherImage);
        new ForecastQuery().execute();

    }


    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        String min;
        String max;
        String current;
        String iconName;
        Bitmap picture;

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inStream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(inStream, "UTF8");

                int type = XmlPullParser.START_DOCUMENT;//Sets type to start of document

                while (type != XmlPullParser.END_DOCUMENT) {//If it is not the end of the document
                    switch (type) {//switch for type
                        //If the current XML event is the start of the document
                        case XmlPullParser.START_DOCUMENT:
                            Log.d(ACTIVITY_NAME, "XML Start document");
                            break;
                        //If the current XML event is the end of the document
                        case XmlPullParser.END_DOCUMENT:
                            Log.d(ACTIVITY_NAME, "XML End document");
                            break;
                        //If the current XML event is an opening tag
                        case XmlPullParser.START_TAG:
                            String name = xpp.getName();//Name of the tag
                            Log.d(ACTIVITY_NAME, "XML Start tag:" + name);
                            if (name.equals("temperature")) {//If the tag name is temperature
                                current = xpp.getAttributeValue(null, "value");
                                publishProgress(75);    //PublishProgress that values were received
                            }
                            if (name.equals("weather")) {//if the tag is weather
                                iconName = xpp.getAttributeValue(null, "icon");//get the icon name
                                if (iconName != null) {//If there is an icon name
                                    if (fileExistance(iconName + ".png")) {//check if file under that name exists
                                        Log.i(ACTIVITY_NAME,"Image already exists");
                                        picture = loadImg(ctx, iconName + ".png");//load the image under that name to Bitmap Picture
                                        publishProgress(100);//publishprogress complete weather
                                    } else {//If the file doesnt exist
                                        Log.i(ACTIVITY_NAME,"Downloading image.");
                                        HttpURLConnection connection = null;//Connection object
                                        try {
                                            URL imgUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");//Url to download image from
                                            connection = (HttpURLConnection) imgUrl.openConnection();//Establish connection
                                            connection.connect();
                                            int responseCode = connection.getResponseCode();//If the response is OK
                                            if (responseCode == 200) {
                                                picture = BitmapFactory.decodeStream(connection.getInputStream());//Download the image
                                                saveFile(ctx, picture, iconName + ".png");//Save the image
                                            }
                                        } catch (Exception e) {
                                            Log.e("Image Exception: ", e.getMessage());
                                        } finally {
                                            if (connection != null) {//After download publish that file was downloaded and disconnect
                                                publishProgress(100);
                                                connection.disconnect();
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        //If the current XML event is an closing tag
                        case XmlPullParser.END_TAG:
                            Log.d(ACTIVITY_NAME, "XML End tag:" + xpp.getName());
                            break;
                        //If the current XML event text between opening and closing tags
                        case XmlPullParser.TEXT:
                            Log.d(ACTIVITY_NAME, "XML text:" + xpp.getText());
                            break;
                    }
                    type = xpp.next();
                }
            } catch (Exception e) {
                Log.e("XML Parsing Exception: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            currentG.setText(current);
            wtImage.setImageBitmap(picture);
            pBar.setVisibility(View.INVISIBLE);
        }
    }


    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void saveFile(Context ctx, Bitmap b, String name) {
        FileOutputStream fos;
        try {
            fos = ctx.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("File or Write Error:", e.getMessage());
        }
    }

    public Bitmap loadImg(Context ctx, String name) {
        Bitmap img = null;
        FileInputStream fis;
        try {
            fis = ctx.openFileInput(name);
            img = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (Exception e) {
            Log.e("File exception: ", e.getMessage());
        }
        return img;
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
                intent = new Intent(Weather.this,Car.class);
                startActivity(intent);
                break;
            case R.id.kitchenMenu:
                Log.i(ACTIVITY_NAME," Kitchen Selected");
                intent = new Intent(Weather.this,Kitchen.class);
                startActivity(intent);
                break;
            case R.id.livingMenu:
                Log.i(ACTIVITY_NAME," Living Room Selected");
                intent = new Intent(Weather.this,LivingRoom.class);
                startActivity(intent);
                break;
            case R.id.homeMenu:
                Log.i(ACTIVITY_NAME," Home Selected");
                intent = new Intent(Weather.this,Home.class);
                startActivity(intent);
                break;
            case R.id.backMenu:
                Log.i(ACTIVITY_NAME," Back Selected");
                backButton();
                break;
            case R.id.helpMenu:
                Log.i(ACTIVITY_NAME," Help Selected");
                android.support.v7.app.AlertDialog.Builder about = new android.support.v7.app.AlertDialog.Builder(Weather.this);
                about.setTitle("About");
                about.setMessage("Version 1.0, by Calvin Williams \n\nDisplays Current Weather.");

                about.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                about.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void backButton(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Weather.this);
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
