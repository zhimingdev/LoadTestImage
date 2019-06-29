package com.example.rxjavaandretrofit;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rxjavaandretrofit.api.Http;
import com.example.rxjavaandretrofit.bean.Man;
import com.example.rxjavaandretrofit.bean.Preson;
import com.example.rxjavaandretrofit.bean.Women;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private Button update;
    private ImageView imageView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv_main);
        button = (Button) findViewById(R.id.but_add);
        update = (Button) findViewById(R.id.but_update);
        imageView = (ImageView) findViewById(R.id.iv_load);
        initLisenter();

//        Man man = new Man();
//        man.setMan("dfasd");
//        man.setName("fae");
//        List<Preson> list = new ArrayList<>();
//        list.add(man);
////        show(list);
//        System.out.println("数据 == >>"+ new Gson().toJson(list));
//        Women women = new Women();
//        women.setName("fdas");
//        women.setWomen("fdasf");
//        List<Preson> lists = new ArrayList<>();
//        lists.add(women);
//        show(lists);

    }

//    private void show(List<Preson> list) {
//        Man man = (Man) list;
//        man.getMan();
//        man.getName();
//    }

    private void initLisenter() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setId(9);
                user.setName("fadsfasf");
                user.setPassword("064561");
                user.setAge(33);
                Http http = new Http();
                http.retrofitService.adduser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<HttpResult<User>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpResult<User> userHttpResult) {
                                String msg = userHttpResult.getMsg();
                                User user1 = userHttpResult.getData();
                                textView.setText(user1.getId()+"\n"+user1.getName()+"\n"
                                        +user1.getPassword()+"\n"+user1.getAge());
                                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                MainActivity.this.startActivityForResult(intent,0);

            }
        });


        String a = "{\"name\":\"小明\",\"id\":\"2616\"}";
        System.out.println("---a: "+a);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            imagePath = getFilePathFromUri(MainActivity.this, uri);
            System.out.println("---图片路径: "+ imagePath);
            Http http = new Http();
            File file = new File(imagePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture",file.getName(),requestBody);
            http.retrofitService.load(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HttpResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpResult httpResult) {
                            Toast.makeText(MainActivity.this,httpResult.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    });
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("-----------");
        }
    }

    /**
     * 根据URI获取图片的绝对路径
     * @param context
     * @param uri
     * @return
     */
    public String getFilePathFromUri(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns
                    .DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
