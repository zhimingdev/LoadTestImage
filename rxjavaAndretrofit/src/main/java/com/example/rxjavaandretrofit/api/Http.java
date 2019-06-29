package com.example.rxjavaandretrofit.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Http {

    public static OkHttpClient okHttpClient;
    public RetrofitService retrofitService;
    public final Retrofit retrofit;

    public Http() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.1.100.1:8088/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60000, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}
