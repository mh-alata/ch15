package com.example.ch15;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView temperature, description, main,humidity,sunrise,sunset;
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

        humidity = (TextView) findViewById(R.id.humidity);;
        sunrise = (TextView) findViewById(R.id.sunRise);;
        sunset = (TextView) findViewById(R.id.sunSet);;



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
                            temperature.setText(String.valueOf(temp)+" C");

                            double humd= jsonMain.getDouble("humidity");
                            humidity.setText("Humidity : "+ String.valueOf(humd));

                            JSONObject jsonsys = response.getJSONObject("sys");
                            String sunRise= jsonsys.getString("sunrise");
                            String sunSet= jsonsys.getString("sunset");

                            sunrise.setText("Sunrise : "+String.valueOf(sunRise));
                            sunset.setText("Sunset : "+String.valueOf(sunSet));





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
                                    else if(wheather.equals("Rainy")){
                                        Glide.with(MainActivity.this).load("https://www.novinite.com/media/images/2017-04/photo_verybig_179672.jpg").into(weatherBackground);

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