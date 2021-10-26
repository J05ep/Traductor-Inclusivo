package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity: ","Opencv is loaded");
        }
        else {
            Log.d("MainActivity: ","Opencv failed to load");
        }
    }

    private Button texto_button;
    private Button combinar_letras;
    private Button historial;
    public static final String nombres = "names";
    public static final String id = "ids";
    TextView cajaBienvenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto_button=findViewById(R.id.texto_button);
        combinar_letras=findViewById(R.id.combinar_letras);
        historial=findViewById(R.id.historial);
        cajaBienvenido=findViewById(R.id.txtBienvenido);
        String usuario = getIntent().getStringExtra("names");
        String id = getIntent().getStringExtra("ids");
        cajaBienvenido.setText("Bienvenido " + usuario);
        String date = new  SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        texto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Texto.class);
                intent.putExtra(Texto.id, id);
                intent.putExtra(Texto.fecha, date);
                startActivity(intent);
            }
        });

        combinar_letras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CombinarLetras.class);
                intent.putExtra(CombinarLetras.id, id);
                intent.putExtra(CombinarLetras.fecha, date);
                startActivity(intent);
            }
        });

        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.imagepro.historial.class);
                intent.putExtra(com.example.imagepro.historial.idU, id);
                startActivity(intent);
            }
        });
    }

}