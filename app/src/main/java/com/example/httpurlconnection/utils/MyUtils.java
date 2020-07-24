package com.example.httpurlconnection.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.pixplicity.sharp.Sharp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyUtils {

    private static OkHttpClient httpClient;

    public static void fetchSvg(Context context, String url, final ImageView target) {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1014))
                    .build();
        }

        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
//                target.setImageDrawable(R.drawable.ic_image_black_24dp);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream stream = Objects.requireNonNull(response.body()).byteStream();
                Sharp.loadInputStream(stream).into(target);
                stream.close();
            }
        });
    }
}
