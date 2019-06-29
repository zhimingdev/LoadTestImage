package com.example.rxjavaandretrofit.api;

import com.example.rxjavaandretrofit.HttpResult;
import com.example.rxjavaandretrofit.User;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface RetrofitService {

    @POST("add")
    Observable<HttpResult<User>> adduser(@Body User user);

    @Multipart
    @POST("uploadimage")
    Observable<HttpResult> load(@Part MultipartBody.Part file);
}
