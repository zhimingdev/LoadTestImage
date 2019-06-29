package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private int i;
    private InputStream inputStream;
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv_image);
        i = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(
                i + 1, i * 2 + 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        initView();
//        initTask();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
    }

    /**
     * 创建线程池下载图片
     */
    private void initView() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("当前线程--" + Thread.currentThread().getName());
                        URL url = new URL("http://hbimg.huabanimg.com/7597b2e1e70f32575ba9f09f7b0986cfdb99dc181cd9d-d47Nog_fw658");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        inputStream = httpURLConnection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /**
     * AsyncTask下载图片
     */
    private void initTask() {
        AsyncTask<String, Void, Bitmap> asyncTask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... voids) {
                URL url = null;
                Bitmap bitmap = null;
                String s = voids[0];
                try {
                    url = new URL(s);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imageView.setImageBitmap(bitmap);
            }


        };
        asyncTask.execute("http://hbimg.huabanimg.com/7597b2e1e70f32575ba9f09f7b0986cfdb99dc181cd9d-d47Nog_fw658");
    }

}
