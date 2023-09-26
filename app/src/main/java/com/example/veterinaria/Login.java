package com.example.veterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btLogin, btAbrirRegistrarCliente;
    EditText etDni, etContrasena;
    int idcliente;
    String dni, contrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadUI();
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });
        btAbrirRegistrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistarCliente.class));
            }
        });
    }
    private void loadUI(){
        btLogin = findViewById(R.id.btLogin);
        etContrasena = findViewById(R.id.etContrasena);
        etDni = findViewById(R.id.etDni);
        btAbrirRegistrarCliente = findViewById(R.id.btAbrirRegistrarCliente);
    }
    private void  validar(){
        dni = etDni.getText().toString().trim();
        contrasena = etContrasena.getText().toString().trim();
        if(dni.isEmpty()){
            etDni.setError("Ingrese un dni");
            etDni.requestFocus();
        }else if(contrasena.isEmpty()){
            etContrasena.setError("Ingrese una contraseña");
            etContrasena.requestFocus();
        }else {
            login();
        }
    }
    private void login(){
        Uri.Builder urlnueva = Uri.parse(Utilidades.URL+"cliente.php").buildUpon();
        urlnueva.appendQueryParameter("operacion", "login");
        urlnueva.appendQueryParameter("dni", dni);
        urlnueva.appendQueryParameter("claveAcceso", contrasena);

        String URLN = urlnueva.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLN, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.i("idcliente", String.valueOf(response.getInt("idcliente")));
                    boolean login = response.getBoolean("login");
                    if (login) {
                        idcliente = response.getInt("idcliente");
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("idcliente", idcliente);
                        startActivity(intent);
                    } else {
                        toast(response.getString("mensaje"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Volley", error.toString());
            }
        });

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, URlUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean login = jsonObject.getBoolean("login");
                    //idcliente = jsonObject.getInt("idCliente");
                    //Log.d("Idcliente", String.valueOf(idcliente));
                    if (login) {
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        //intent.putExtra("idcliente", idcliente);
                        startActivity(intent);
                    } else {
                        toast("Usuario o contraseña incorrecta");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("Algo anda mal");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("operacion", "login");
                parametros.put("dni", dni);
                parametros.put("claveAcceso", contrasena);
                return parametros;
            }
        };*/
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }
    private void toast(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}