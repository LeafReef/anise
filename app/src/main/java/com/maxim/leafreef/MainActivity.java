package com.maxim.leafreef;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    int temperature, humidity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        getSensorData();
    }

    public void getSensorData() {
        String url = "https://leafreef.herokuapp.com/api/list";

        TextView tempText = (TextView) findViewById(R.id.temperature_text);
        TextView humText = (TextView) findViewById(R.id.humidity_text);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            temperature = response.getInt("temperature");
                            humidity = response.getInt("humidity");

                            System.out.println(temperature);

                            tempText.setText(temperature + "°C");
                            humText.setText(humidity + "%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                System.out.println("Error getting data");
                e.printStackTrace();
            }
        });

        queue.add(request);
    }

    public void sendPostRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void refreshSensorData(View view) {
        getSensorData();
    }

    public void toggleOn(View view) {
        String onUrl = "http://192.168.1.8:3000/on";

        sendPostRequest(onUrl);
    }

    public void toggleOff(View view) {
        String onUrl = "http://192.168.1.8:3000/off";

        sendPostRequest(onUrl);
    }
}