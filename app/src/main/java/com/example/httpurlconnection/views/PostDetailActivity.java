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
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostDetailActivity extends AppCompatActivity {

    EditText userNameEt;
    Button submitBtn;
    String userName;
    String stringUrl;
    URL myUrl;
    HttpURLConnection httpURLConnection;
    ProgressDialog progressDialog;
    byte[] postData;
    BufferedReader bufferedReader;

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
        stringUrl = MyAppURLs.saveUser;
        JSONObject myData = new JSONObject();
        try {
            myData.put("name", userName);
            myData.put("job", "Leader");
            postData = myData.toString().getBytes(StandardCharsets.UTF_8);
            new PostUserData(this).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class PostUserData extends AsyncTask<String, String, String> {
        WeakReference<PostDetailActivity> weakReference;

        PostUserData(PostDetailActivity context) {
            weakReference = new WeakReference<>(context);
        }

        String userData = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PostDetailActivity postDetailActivity = weakReference.get();
            postDetailActivity.progressDialog.setMessage("Saving User...");
            postDetailActivity.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            PostDetailActivity postDetailActivity = weakReference.get();

            try {
                postDetailActivity.myUrl = new URL(postDetailActivity.stringUrl);
                postDetailActivity.httpURLConnection = (HttpURLConnection) postDetailActivity.myUrl
                        .openConnection();
                postDetailActivity.httpURLConnection.setDoOutput(true);
                postDetailActivity.httpURLConnection.setRequestMethod("POST");
                postDetailActivity.httpURLConnection.setRequestProperty(
                        "Accept", "application/json");
                postDetailActivity.httpURLConnection.setRequestProperty(
                        "Content-Type", "application/json; utf-8");
                DataOutputStream dataOutputStream = new DataOutputStream(postDetailActivity
                        .httpURLConnection.getOutputStream());
                dataOutputStream.write(postDetailActivity.postData);
                postDetailActivity.httpURLConnection.connect();

                int responseCode = postDetailActivity.httpURLConnection.getResponseCode();
                if (responseCode == 201) {
                    postDetailActivity.bufferedReader = new BufferedReader(new
                            InputStreamReader(postDetailActivity.httpURLConnection.getInputStream()));
                    String line = "";
                    while ((line = postDetailActivity.bufferedReader.readLine()) != null) {
                        userData += line;
                    }
                } else {
                    Log.d("RESPMES", postDetailActivity.httpURLConnection.getResponseMessage());

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (postDetailActivity.bufferedReader != null) {
                    try {
                        postDetailActivity.bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (postDetailActivity.httpURLConnection != null) {
                    postDetailActivity.httpURLConnection.disconnect();
                }
            }
            return userData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PostDetailActivity postDetailActivity = weakReference.get();
            postDetailActivity.progressDialog.dismiss();
            Log.d("USERDETAILS", s);
            try {
                JSONObject catObj = new JSONObject(s);
                if (catObj.has("createdAt")) {

                    if (!catObj.getString("createdAt").isEmpty()) {
                        Toast.makeText(postDetailActivity, postDetailActivity.getString(
                                R.string.toast1 )+ " "+
                                        catObj.getString("id"),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(postDetailActivity, "Empty Field",
                                Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
