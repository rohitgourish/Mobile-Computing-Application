package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String state;
    String url = "http://192.168.0.91:8000" + "/upload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encoded = Base64.getEncoder().encodeToString(byteArray);
        ImageView image;
        image = findViewById(R.id.imageView);
        image.setImageBitmap(bitmap);


//        String[] arraySpinner = new String[]{
//                "Men", "Women", "Toys", "Gadgets"
//        };
//        Spinner s = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, arraySpinner);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        s.setAdapter(adapter);
        Button bt = (Button) findViewById(R.id.button2);
//        s.setOnItemSelectedListener(this);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OkHttpClient client=new OkHttpClient();
                RequestBody f=new FormBody.Builder()
                        .add("category",encoded)
                        .build();
                Request request=new Request.Builder().url(url).post(f).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        state=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}



