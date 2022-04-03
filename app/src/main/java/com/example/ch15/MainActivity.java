package com.example.ch15;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView temperature, description, main,humidity,sunrise,sunset,feels,windSpeed;
    // we"ll make HTTP request to this URL to retrieve weather conditions
    String weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q=riyadh&appid=1d47b8e245e55c478c2f352cf0299029&units=metric";
    ImageView weatherBackground;
    // Textview to show temperature and description


    // JSON object that contains weather information
    JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        temperature = (TextView) findViewById(R.id.temperature);
        description = (TextView) findViewById(R.id.description);
        main = (TextView) findViewById(R.id.main);

        humidity = (TextView) findViewById(R.id.humidity);
        sunrise = (TextView) findViewById(R.id.sunRise);
        sunset = (TextView) findViewById(R.id.sunSet);
        feels= (TextView) findViewById(R.id.feels);
        windSpeed= (TextView) findViewById(R.id.windSpeed);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String city = adapterView.getItemAtPosition(i).toString();
                weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        city +
                        "&appid=1d47b8e245e55c478c2f352cf0299029&units=metric";

                wheather(weatherWebserviceURL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        weatherBackground = (ImageView) findViewById(R.id.weatherbackground);
        wheather(weatherWebserviceURL);





    }

    public void wheather(String url){
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Gorge","Response  Received");
                        Log.d("Gorge",response.toString());
                        try {

                            String town = response.getString("name");
                            Log.d("Gorge", town);
                            description.setText(town);

                            JSONObject jsonMain = response.getJSONObject("main");
                            double temp= jsonMain.getDouble("temp");
                            Log.d("Gorge-temp", String.valueOf(temp));
                            temperature.setText(String.valueOf(temp)+"\u2103");

                            double humd= jsonMain.getDouble("humidity");
                            humidity.setText("Humidity : "+ String.valueOf(humd+"%"));

                            JSONObject jsonsys = response.getJSONObject("sys");
                            Long sunRise= jsonsys.getLong("sunrise");
                            Long sunSet= jsonsys.getLong("sunset");


                            SimpleDateFormat dtFormate = new SimpleDateFormat("HH:mm");
                            Date time_R= new Date(sunRise*1000);
                            Date time_S= new Date(sunSet*1000);

                            String timesunrise=dtFormate.format(time_R);
                            String timesunset=dtFormate.format(time_S);

                            sunrise.setText("Sunrise : "+String.valueOf(timesunrise));
                            sunset.setText("Sunset : "+String.valueOf(timesunset));

                            double feel= jsonMain.getDouble("feels_like");
                            Log.d("Gorge-temp", String.valueOf(feel));
                            feels.setText("feels_like : "+String.valueOf(feel)+"\u2103");


                            JSONObject jsonwind = response.getJSONObject("wind");
                            double wind_Speed= jsonwind.getDouble("speed");
                            windSpeed.setText("wind Speed : "+String.valueOf(wind_Speed) + "m/s");


                            JSONArray jsonArray = response.getJSONArray("weather");
                            for (int i=0; i<jsonArray.length(); i++){
                                    Log.d("Gorge-Array", jsonArray.getString(i));
                                    JSONObject oneObject= jsonArray.getJSONObject(i);
                                    String wheather = oneObject.getString("main");
                                    Log.d("Gorge-W", wheather);
                                main.setText(wheather);

                                    if(wheather.equals("Clear")){
                                        Glide.with(MainActivity.this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFi4WPOG5gngGFelZNREOatVdmXlsq3Tc3rQ&usqp=CAU").into(weatherBackground);

                                    }
                                    else if(wheather.equals("Clouds")){
                                        Glide.with(MainActivity.this).load("https://media.istockphoto.com/photos/cloudy-sky-background-picture-id1064921790?k=20&m=1064921790&s=612x612&w=0&h=pED-trQGoNjIo95vOK15bRUsS_UahOuFYgUN8578ynA=").into(weatherBackground);
                                    }
                                    else if(wheather.equals("Rain")){
                                        Glide.with(MainActivity.this).load("https://www.novinite.com/media/images/2017-04/photo_verybig_179672.jpg").into(weatherBackground);

                                    }
                                    else if(wheather.equals("Snow")){
                                        Glide.with(MainActivity.this).load("https://media.wired.co.uk/photos/606da1f02a09c24efb8f43c6/master/w_1600%2Cc_limit/iStock-613015246.jpg").into(weatherBackground);

                                    }
                                    else{
                                        Glide.with(MainActivity.this).load("https://ktar.com/wp-content/uploads/2019/02/phoenix-weather.jpg").into(weatherBackground);
                                    }

                            }


                        }
                        catch (JSONException e){

                            e.printStackTrace();
                            Log.e("Recieve Error", e.toString());
                            temperature.setText(e.toString());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Gorge","Error retrieving URL");

                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);

    }
}