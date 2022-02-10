package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    //Déclaration des Champs
    TextView mDate, mCity, mTemp, mDescription;
    ImageView imgIcon;
    String maVille = "Toronto";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDate = findViewById(R.id.mDate);
        mCity = findViewById(R.id.mCity);
        mTemp = findViewById(R.id.mTemp);
        mDescription = findViewById(R.id.mDescription);
        afficher();
    }

    // Method pour nous donner les elements dans le JSON file
    public void afficher()
    {
     String url = "http://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=c6c243680be264b74da50a29d73b3f0f&units=metric";
     JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
             new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject response)
         {
             try {
                 JSONObject main_object=response.getJSONObject("main"); // Il va dans l'objet "main" et prend les elements de la
                 JSONArray array = response.getJSONArray("weather"); // Il va dans le tableau "weather" puis prend les elements dans le tableau
                 Log.d("Tag", "resultat= "+array.toString()); // Affiche les elements du array "weather" dans le logcat
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

         }
     });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
}
