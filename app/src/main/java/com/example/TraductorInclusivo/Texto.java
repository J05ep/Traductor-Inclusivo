package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Texto extends AppCompatActivity {

    String IP = "192.168.0.11";
    public static final String id = "ids";
    public static final String fecha = "fechas";
    private EditText edtTexto;
    private Button edtTraducir;
    private String traducir;
    private VideoView videoView;
    private TextView change_text;
    private WebView webView;
    private String final_text = "";
    private String esp = " ";
    private String palabra = "";
    char letra;
    private String letra2 = "";
    private String[] arrayTraducir;

    int i = 0;
    int b = 0;
    int tamaño = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto);

        edtTexto = findViewById(R.id.edtTexto);
        edtTraducir = findViewById(R.id.edtTraducir);
        videoView = (VideoView) findViewById(R.id.vv_test);
        change_text = findViewById(R.id.change_text);
        String id = getIntent().getStringExtra("ids");
        String fecha = getIntent().getStringExtra("fechas");

        edtTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                b=0;
                letra2="";
                final_text = "";
                traducir = edtTexto.getText().toString();
                traducir = traducir.toLowerCase();
                final_text = "";
                change_text.setText(final_text);

                if(!traducir.isEmpty()){
                    if(traducir.equals("cuál es tu nombre") || traducir.equals("cómo te llamas")){
                        traducir = "tu nombre";
                    }
                    validarVideos();
                    guardarHistorial("http://"+IP+"/traductor/historial.php?id="+id+"&traducir="+traducir+"&fecha="+fecha);
                }else{
                    Toast.makeText(Texto.this, "Por favor escriba lo que se desea traducir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarVideos(){
        arrayTraducir = traducir.split(" ");
        palabra = arrayTraducir[i];
        tamaño = arrayTraducir.length;
        System.out.println(tamaño);

        System.out.println(palabra);
        if(palabra.equals("otra")){

            System.out.println(arrayTraducir[i+1]);
            if(arrayTraducir[i+1]==null){
                System.out.println("holaaaa");
                i++;
                palabra = "otravez";
            }else{
                palabra = "otra";
            }
        }

        if(palabra.equals("por")){
            if(arrayTraducir[i+1].equals("favor")){
                i++;
                palabra = "porfavor";
            }else{
                palabra = "por";
            }
        }
        validarVideosPHP("http://"+IP+"/traductor/validarVideo.php?palabra="+palabra);
    }

    private void validarVideosPHP(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    videoView.setVideoPath("http://"+IP+"/traductor/Animaciones/" + palabra + ".mp4");
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            final_text = final_text + palabra + esp;
                            change_text.setText(final_text);
                            if((i + 1) < arrayTraducir.length){
                                videos(arrayTraducir);
                            }
                        }
                    });
                }else{
                    System.out.println(" i : "+i+" tamaño : "+tamaño);
                    if(i < tamaño){
                        deletrear(palabra);
                    }else{
                        System.out.println("papinish finish");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("palabra", palabra);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void deletrear(String palabra){
        char[] arrayLetras = palabra.toCharArray();
        letra = arrayLetras[b];

        //if(letra == 'l'){
          //  if(arrayLetras[b+1] == 'l'){
            //    b++;
              //  letra2 = "ll";
            //}
        //}
        //if(letra == 'r'){
            //if(arrayLetras[b+1] == 'r'){
              //  b++;
                //letra2 = "rr";
           // }
       // }
       // if(letra2 != ""){
         //   videoView.setVideoPath("http://192.168.43.132/traductor/AnimacionesABC/" + letra2 + ".mp4");
        //}else{
            videoView.setVideoPath("http://"+IP+"/traductor/AnimacionesABC/" + letra + ".mp4");
        //}
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(letra2 != ""){
                    final_text = final_text + letra2;
                    change_text.setText(final_text);
                    letra2 = "";
                }else{
                    final_text = final_text + letra;
                    change_text.setText(final_text);
                }
                if((b + 1) < arrayLetras.length){
                    videosABC(arrayLetras);
                }
            }
        });
    }

    private void videosABC(char[] arrayLetras){
        b++;
        letra = arrayLetras[b];


            videoView.setVideoPath("http://"+IP+"/traductor/AnimacionesABC/" + letra + ".mp4");

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(letra2 != ""){
                    final_text = final_text + letra2;
                    change_text.setText(final_text);
                    letra2 = "";
                }else{
                    final_text = final_text + letra;
                    change_text.setText(final_text);
                }
                if((b + 1) < arrayLetras.length){
                    videosABC(arrayLetras);
                }else{
                    if((i+1) < tamaño){
                        final_text = final_text + esp;
                        change_text.setText(final_text);
                        b=0;
                        i++;
                        palabra = arrayTraducir[i];
                        validarVideosPHP("http://"+IP+"/traductor/validarVideo.php?palabra="+palabra);
                    }
                }
            }
        });

    }

    private void videos(String[] arrayTraducir){
        i++;
        palabra = arrayTraducir[i];

        validarVideosPHP("http://192.168.0.10/traductor/validarVideo.php?palabra="+palabra);
    }

    private void guardarHistorial(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    //Toast.makeText(Texto.this, "El texto se guardo en el historial", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(Texto.this, "Correo o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Texto.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id", id);
                parametros.put("traducir", traducir);
                parametros.put("fecha", fecha);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}