package com.example.httpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.but_net);
        textView = (TextView) findViewById(R.id.tv_content);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        initHttp();
                    }
                }.start();

            }
        });

    }

    private void initHttp() {
        try {
            URL url = new URL("http://10.1.100.15:8080/user");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setRequestProperty("Content-type","application/x-javascript->json");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            final StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("---:  ");
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] data = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(data))!= -1) {
                String s = new String(data, Charset.forName("utf-8"));
                stringBuffer.append(s);
                final String string = stringBuffer.toString();
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(string);
                    }
                });
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
