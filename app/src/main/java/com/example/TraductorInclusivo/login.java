package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    String IP = "192.168.0.11";
    EditText edtCorreo, edtPassword;
    Button btnLogin, btnRegistrar;
    String correo, password;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtPassword = findViewById((R.id.edtPassword));
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        rq = Volley.newRequestQueue(this);

        recuperarPreferencias();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo = edtCorreo.getText().toString();
                password = edtPassword.getText().toString();

                if(!correo.isEmpty() && !password.isEmpty()){
                    validarUsuario("http://"+IP+"/traductor/validar_usuario.php");
                }else{
                    Toast.makeText(login.this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registrar.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    iniciarSesion();
                }else{
                    Toast.makeText(login.this, "Correo o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(login.this, error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("correo", correo);
                parametros.put("password", password);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo",correo);
        editor.putString("password", password);
        editor.putBoolean("sesion", true);
        editor.commit();

    }

    private void recuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        edtCorreo.setText(preferences.getString("correo", "micorreo@alumnos.udg.mx"));
        edtPassword.setText(preferences.getString("password", "12345"));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(login.this, "No pudo iniciar sesión", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(login.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
        usuario usu = new usuario();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        try{
            jsonObject = jsonArray.getJSONObject(0);
            usu.setId(jsonObject.optString("id"));
            usu.setUsuario(jsonObject.optString("usuario"));
            usu.setCorreo(jsonObject.optString("correo"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        guardarPreferencias();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.nombres, usu.getUsuario());
        intent.putExtra(MainActivity.id, usu.getId());
        startActivity(intent);
        finish();
    }

    private void iniciarSesion(){
        String url = "http://"+IP+"/traductor/sesion.php?correo="+correo;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
        System.out.println(url);
    }
}