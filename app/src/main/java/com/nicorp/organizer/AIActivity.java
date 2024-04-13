package com.nicorp.organizer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AIActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aiactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String token = "M2VhZGJkN2YtNmZiZS00ZGI3LWI4MjgtNzMzMTE2ODViYzYxOmMzOGVmN2YyLTFkYTUtNDYyOS1iZjBmLWE2NTRhYjdhMDNlOQ==";

        AsyncTask.execute(() -> {
            // Perform network operations here
            try {

                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
                newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
                newBuilder.hostnameVerifier((hostname, session) -> true);

                OkHttpClient client = newBuilder.build();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Basic " + token)
                        .addHeader("RqUID", "c38ef7f2-1da5-4629-bf0f-a654ab7a03e9")
                        .build();
                Response response = client.newCall(request).execute();

                Log.d("token", response.message());
                Log.d("token", "ssss");

                OkHttpClient client1 = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType1 = MediaType.parse("application/json");
                RequestBody body1 = RequestBody.create(mediaType1, "{\n  \"model\": \"GigaChat\",\n  \"messages\": [\n    {\n      \"role\": \"user\",\n      \"data_for_context\": [\n        {}\n      ]\n    }\n  ],\n  \"function_call\": {\n    \"name\": \"sbermarket-pizza_order\",\n    \"partial_arguments\": {}\n  },\n  \"functions\": [\n    {\n      \"name\": \"pizza_order\",\n      \"description\": \"Функция для заказа пиццы\",\n      \"parameters\": {},\n      \"few_shot_examples\": [\n        {\n          \"request\": \"Погода в Москва в ближайшие три дня\",\n          \"params\": {}\n        }\n      ],\n      \"return_parameters\": {}\n    }\n  ],\n  \"temperature\": 1,\n  \"top_p\": 0.1,\n  \"n\": 1,\n  \"stream\": false,\n  \"max_tokens\": 512,\n  \"repetition_penalty\": 1,\n  \"update_interval\": 0\n}");
                Request request1 = new Request.Builder()
                        .url("https://gigachat.devices.sberbank.ru/api/v1/chat/completions")
                        .method("POST", body1)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer <TOKEN>")
                        .build();
                Response response1 = client1.newCall(request1).execute();

                Log.d("Response", "onSuccess: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}