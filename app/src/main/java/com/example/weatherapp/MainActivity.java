package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    //Déclaration des Champs
    TextView mDate, mCity, mTemp, mDescription,mWindValue, mFeelsLike, mHigh, mLow;
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
        mWindValue = findViewById(R.id.mWindValue);
        mFeelsLike = findViewById(R.id.mFeelsLike);
        mHigh = findViewById(R.id.mHigh);
        mLow = findViewById(R.id.mLow);


        afficher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Écrire le nom de la ville"); // dit a l'usager d'écrire le nom da la ville
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                maVille = query; // insère le text que l'usager a écrit dans le string "maVille"
                afficher(); // afficher les données pour ce ville
                // gestion du clavier
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus() !=null)
                {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Method pour nous donner les elements dans le JSON file
    public void afficher()
    {
     String url = "http://api.openweathermap.org/data/2.5/weather?q="+ maVille +"&appid=c6c243680be264b74da50a29d73b3f0f&units=metric";//met le string "maVille" là pour que l'usager peut changer cette variable dans le search view
     JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
             new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject response)
         {
             try {
                 JSONObject main_object=response.getJSONObject("main"); // Il va dans l'objet "main" et prend les elements de là
                 JSONArray array = response.getJSONArray("weather"); // Il va dans le array "weather" puis prend les elements dans le tableau
                 JSONObject wind = response.getJSONObject("wind");
                 //Log.d("Tag", "resultat= "+array.toString()); // Affiche les elements du array "weather" dans le logcat
                 JSONObject object = array.getJSONObject(0); // va dans le première élément

                 // Température:
                 int tempC = (int)Math.round(main_object.getDouble("temp")); // va prend le valeur du "temp" et le convertir en integer
                 String temp = String.valueOf(tempC); // convertir le nombre qu'on a arrondir en String

                 int highC = (int)Math.round(main_object.getDouble("temp_max")); // prend le high pour le temperature et le convertir en integer
                 String high = String.valueOf(highC);

                 int lowC = (int)Math.round(main_object.getDouble("temp_min")); // prend le minimum pour le temperature dans l'object "main" et le convertir en integer
                 String low = String.valueOf(lowC);

                 int feelsC = (int)Math.round(main_object.getDouble("feels_like"));
                 String feels = String.valueOf(feelsC);

                 // Vent:
                 int windC = (int)Math.round(wind.getDouble("speed"));
                 String speed = String.valueOf(windC);

                 String description = object.getString("description");
                 String city = response.getString("name");
                 String icon = object.getString("icon");

                 //Pour mettre tous les valeurs qu'on a crée ci-dessus dans les champs
                 mCity.setText(city);
                 mTemp.setText(temp);
                 mHigh.setText(high);
                 mLow.setText(low);
                 mWindValue.setText(speed);
                 mFeelsLike.setText(feels);
                 mDescription.setText(description);

                 //Formattage du temp
                 Calendar calendar = Calendar.getInstance();
                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd");
                 String formatted_date = simpleDateFormat.format(calendar.getTime());

                 //Mettre le string pour le temp qu'on a crée dans le correct champ
                 mDate.setText(formatted_date);

                 // Gestion de l'image
                 String imageUri = "http://openweathermap.org/img/w/"+ icon + ".png";
                 imgIcon = findViewById(R.id.imgIcon);
                 Uri myUri = Uri.parse(imageUri); //transform en Uri
                 Picasso.with(MainActivity.this).load(myUri).resize(200, 200).into(imgIcon);

             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

         }
     });
        // ajouter tous les éléments à la queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
}
