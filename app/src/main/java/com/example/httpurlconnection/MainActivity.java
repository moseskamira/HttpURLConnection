package com.example.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Country> countryArrayList;
    ProgressDialog progressDialog;
    HttpURLConnection connection;
    String countryName, countryCapital, alphaCode, callingCode, region, population, countryFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.countries_recycler_view);
        countryArrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        fetchAllCountries();
    }

    private void fetchAllCountries() {
        new GetAllCountries().execute();


    }

    private void initializeRecyclerView(ArrayList<Country> myList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CountriesAdapter(this, myList));

    }


    private class GetAllCountries extends AsyncTask<String, String, String> {
        String allCountriesData = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL myUrl = new URL(MyAppURLs.fetchAllCountries);
                connection = (HttpURLConnection) myUrl.openConnection();
                int code = connection.getResponseCode();
                if (code ==  200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        allCountriesData+=line;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return allCountriesData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d("VERIFYME", s);

            try {

                JSONArray myArray = new JSONArray(s);

                for (int j = 0; j<myArray.length(); j++) {
                    JSONObject firstObj = myArray.getJSONObject(j);
                    countryName = firstObj.getString("name");
                    countryCapital = firstObj.getString("capital");
                    alphaCode = firstObj.getString("alpha2Code");
                    JSONArray callCode = firstObj.getJSONArray("callingCodes");
                    callingCode = callCode.get(0).toString();
                    Log.d("CODE", callingCode);
                    region = firstObj.getString("region");
                    population = firstObj.getString("population");
                    countryFlag = firstObj.getString("flag");

                    Log.d("MYFLAG", countryFlag);

                    Country myCountry = new Country();
                    myCountry.setCountryName(countryName);
                    myCountry.setCountryCapital(countryCapital);
                    myCountry.setCallingCode(callingCode);
                    myCountry.setAlpha2Code(alphaCode);
                    myCountry.setRegion(region);
                    myCountry.setPopulation(population);
                    myCountry.setCountryFlagUrl(countryFlag);

                    countryArrayList.add(myCountry);

                    initializeRecyclerView(countryArrayList);
                }



            }catch (Exception e) {
                e.printStackTrace();
            }




        }
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


}
