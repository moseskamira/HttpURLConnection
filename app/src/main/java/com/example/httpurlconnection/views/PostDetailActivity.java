package com.example.httpurlconnection.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.httpurlconnection.R;
import com.example.httpurlconnection.services.MyAppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostDetailActivity extends AppCompatActivity {

    EditText userNameEt;
    Button submitBtn;
    String userName;
    HttpURLConnection httpURLConnection;
    String appUrl;
    ProgressDialog progressDialog;
    byte[] postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        userNameEt = findViewById(R.id.album_et);
        submitBtn = findViewById(R.id.submit_btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulateAlbumPostData();
            }
        });
    }

    private void formulateAlbumPostData() {
        userName = userNameEt.getText().toString();
        appUrl = MyAppURLs.saveUser;
        JSONObject myData = new JSONObject();
        try {
            myData.put("name", userName);
            myData.put("job", "Leader");

            postData = myData.toString().getBytes(StandardCharsets.UTF_8);
            new PostUserData().execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PostUserData extends AsyncTask<String, String, String> {
        String userData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Saving User...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL myUrl = new URL(appUrl);

                httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.write(postData);
                String outputdata = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection
                        .getInputStream()));
                String line = "";
                while ((line = in.readLine()) != null) {
                    outputdata += line;
                }
                userData = outputdata;
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return userData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d("USERDETAILS", s);
            try {
                JSONObject catObj = new JSONObject(s);
                if (catObj.has("createdAt")) {

                    if (!catObj.getString("createdAt").isEmpty()) {
                        Toast.makeText(PostDetailActivity.this, getString(R.string.toast1),
                                Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(PostDetailActivity.this, "Empty Field Not Allowed",
                                Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
