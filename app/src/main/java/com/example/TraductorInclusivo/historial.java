package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.engine.OpenCVEngineInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class historial extends AppCompatActivity {

    String IP = "192.168.0.11";
    public static final String idU = "ids";
    ListView historialST;
    ListView historialTS;
    ArrayAdapter<String> adapterTS;
    ArrayAdapter<String> adapterST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        historialST = (ListView)findViewById(R.id.historialStoT);
        historialTS = (ListView)findViewById(R.id.historialTtoS);

        adapterTS = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapterST = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        historialTS.setAdapter(adapterTS);
        historialST.setAdapter(adapterST);

        new ConnectionTS().execute();
        new ConnectionST().execute();
    }

    class ConnectionST extends AsyncTask<String, String, String>{
        String ids = getIntent().getStringExtra("ids");
        int id = Integer.parseInt(ids);
        @Override
        protected String doInBackground(String... strings) {
            String resultST = "";
            String hostST = "http://"+IP+"/traductor/mostrarHistorialST.php?id="+id;
            try {
                HttpClient clientST = new DefaultHttpClient();
                HttpGet requestST = new HttpGet();
                requestST.setURI(new URI(hostST));
                HttpResponse responseST = clientST.execute(requestST);
                BufferedReader readerST = new BufferedReader(new InputStreamReader(responseST.getEntity().getContent()));
                StringBuffer stringBufferST = new StringBuffer("");

                String line = "";
                while((line = readerST.readLine()) != null){
                    stringBufferST.append(line);
                    break;
                }
                readerST.close();
                resultST = stringBufferST.toString();

            } catch (Exception e) {
                return new String("There exception: " + e.getMessage());
            }
            return resultST;
        }
        @Override
        protected void onPostExecute(String resultST) {
            try {
                JSONObject jsonResultST = new JSONObject(resultST);
                int successST = jsonResultST.getInt("success");
                if(successST == 1){
                    JSONArray traduccionesST = jsonResultST.getJSONArray("traducciones");
                    for(int i = 0; i < traduccionesST.length(); i++){
                        JSONObject traduccionST = traduccionesST.getJSONObject(i);
                        String texto = traduccionST.getString("texto");
                        String fecha = traduccionST.getString("fecha");
                        String line = texto + " | " + fecha;
                        adapterST.add(line);
                    }
                    //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(), "NO OK", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ConnectionTS extends AsyncTask<String, String, String>{
        String ids = getIntent().getStringExtra("ids");
        int id = Integer.parseInt(ids);
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String hostTS = "http://"+IP+"/traductor/mostrarHistorialTS.php?id="+id;
            try {
                HttpClient clientTS = new DefaultHttpClient();
                HttpGet requestTS = new HttpGet();
                requestTS.setURI(new URI(hostTS));
                HttpResponse responseTS = clientTS.execute(requestTS);
                BufferedReader readerTS = new BufferedReader(new InputStreamReader(responseTS.getEntity().getContent()));
                StringBuffer stringBufferTS = new StringBuffer("");

                String line = "";
                while((line = readerTS.readLine()) != null){
                    stringBufferTS.append(line);
                    break;
                }
                readerTS.close();
                result = stringBufferTS.toString();

            } catch (Exception e) {
                return new String("There exception: " + e.getMessage());
            }
            System.out.println(result);
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                System.out.println(success);
                if(success == 1){
                    JSONArray traducciones = jsonResult.getJSONArray("traducciones");
                    for(int i = 0; i < traducciones.length(); i++){
                        JSONObject traduccion = traducciones.getJSONObject(i);
                        String texto = traduccion.getString("texto");
                        String fecha = traduccion.getString("fecha");
                        String line = texto + " | " + fecha;
                        adapterTS.add(line);
                    }
                    //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(), "NO OK", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}