package com.example.httpurlconnection.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.httpurlconnection.R;
import com.example.httpurlconnection.adapters.CountriesAdapter;
import com.example.httpurlconnection.models.Country;
import com.example.httpurlconnection.services.MyAppURLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Country> countryArrayList;
    ProgressDialog progressDialog;
    HttpURLConnection connection;
    String countryName, countryCapital, alphaCode, callingCode, region, population, countryFlag;

    TextView postLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.countries_recycler_view);
        countryArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        postLink = findViewById(R.id.post_link);
        if (postLink != null) {
            postLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchPostDetailsActivity();
                }
            });
        }


        fetchAllCountries();
    }

    private void launchPostDetailsActivity() {
        startActivity(new Intent(MainActivity.this, PostDetailActivity.class));
    }

    private void fetchAllCountries() {
        new GetAllCountries(this).execute();


    }


    private class GetAllCountries extends AsyncTask<String, String, String> {
        String allCountriesData = "";
        WeakReference<MainActivity> weakReference;

        GetAllCountries(MainActivity context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity mainActivity = weakReference.get();
            mainActivity.progressDialog.setMessage("Fetching Countries....");
            mainActivity.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            MainActivity mainActivity = weakReference.get();

            try {
                URL myUrl = new URL(MyAppURLs.fetchAllCountries);
                mainActivity.connection = (HttpURLConnection) myUrl.openConnection();
                mainActivity.connection.connect();

                int responseCode = mainActivity.connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            mainActivity.connection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        allCountriesData += line;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mainActivity.connection != null) {
                    mainActivity.connection.disconnect();
                }
            }

            return allCountriesData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity mainActivity = weakReference.get();
            mainActivity.progressDialog.dismiss();
            Log.d("COUNTRIES", s);

            try {

                JSONArray myArray = new JSONArray(s);

                for (int j = 0; j < myArray.length(); j++) {
                    JSONObject firstObj = myArray.getJSONObject(j);
                    mainActivity.countryName = firstObj.getString("name");
                    mainActivity.countryCapital = firstObj.getString("capital");
                    mainActivity.alphaCode = firstObj.getString("alpha2Code");
                    JSONArray callCode = firstObj.getJSONArray("callingCodes");
                    mainActivity.callingCode = callCode.get(0).toString();
                    mainActivity.region = firstObj.getString("region");
                    mainActivity.population = firstObj.getString("population");
                    mainActivity.countryFlag = firstObj.getString("flag");

                    Log.d("MYFLAG", mainActivity.countryFlag);

                    Country myCountry = new Country();
                    myCountry.setCountryName(mainActivity.countryName);
                    myCountry.setCountryCapital(mainActivity.countryCapital);
                    myCountry.setCallingCode(mainActivity.callingCode);
                    myCountry.setAlpha2Code(mainActivity.alphaCode);
                    myCountry.setRegion(mainActivity.region);
                    myCountry.setPopulation(mainActivity.population);
                    myCountry.setCountryFlagUrl(mainActivity.countryFlag);

                    mainActivity.countryArrayList.add(myCountry);
                    initializeRecyclerView(mainActivity.countryArrayList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeRecyclerView(ArrayList<Country> myList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CountriesAdapter(this, myList));

    }

}
