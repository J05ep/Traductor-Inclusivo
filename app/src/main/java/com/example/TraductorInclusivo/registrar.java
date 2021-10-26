package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class registrar extends AppCompatActivity {

    String IP = "192.168.0.11";
    EditText edtrUsuario, edtrCorreo, edtrPassword, edtrPassword2;
    String usuario, correo, pass, pass2;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        edtrUsuario = findViewById(R.id.edtrUsuario);
        edtrCorreo = findViewById(R.id.edtrCorreo);
        edtrPassword = findViewById(R.id.edtrPassword);
        edtrPassword2 = findViewById(R.id.edtrPassword2);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = edtrUsuario.getText().toString();
                correo = edtrCorreo.getText().toString();
                pass = edtrPassword.getText().toString();
                pass2 = edtrPassword2.getText().toString();

                if(!usuario.isEmpty() && !correo.isEmpty() && !pass.isEmpty() && !pass2.isEmpty()){
                    if(!pass.equals(pass2)){
                        Toast.makeText(registrar.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }else{
                        if (!validarCorreoForma(correo)){
                            Toast.makeText(registrar.this, "Formato de correo invalido", Toast.LENGTH_SHORT).show();
                        }else{
                            validarCorreo("http://"+IP+"/traductor/validar_registro.php");
                        }
                    }
                }else{
                    Toast.makeText(registrar.this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validarCorreoForma(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void validarCorreo(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Toast.makeText(registrar.this, "El correo ya esta registrado", Toast.LENGTH_SHORT).show();
                } else {
                    registrar("http://"+IP+"/traductor/registrar.php");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(registrar.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario", usuario);
                parametros.put("correo", correo);
                parametros.put("pass", pass);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void registrar(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Toast.makeText(registrar.this, "La cuenta se creó con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(registrar.this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(registrar.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario", usuario);
                parametros.put("correo", correo);
                parametros.put("pass", pass);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}