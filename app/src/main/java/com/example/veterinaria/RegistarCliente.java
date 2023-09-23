package com.example.veterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistarCliente extends AppCompatActivity {
    EditText etApellidos, etNombres, etDni, etClave;
    Button btGuardarCliente;
    String apellidos, nombres, dni, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_cliente);
        loadUI();
        btGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });
    }
    private void loadUI(){
        etApellidos = findViewById(R.id.etApellidos);
        etNombres = findViewById(R.id.etNombres);
        etDni = findViewById(R.id.etDni);
        etClave = findViewById(R.id.etClave);

        btGuardarCliente = findViewById(R.id.btGuardarCliente);
    }
    private void reset(){
        etApellidos.setText("");
        etNombres.setText("");
        etDni.setText("");
        etClave.setText("");
    }

    private void validar(){
        apellidos = etApellidos.getText().toString().trim();
        nombres = etNombres.getText().toString().trim();
        dni = etDni.getText().toString().trim();
        clave = etClave.getText().toString().trim();
        if(apellidos.isEmpty()){
            etApellidos.setError("Complete este campo");
            etApellidos.requestFocus();
        }else  if(nombres.isEmpty()){
            etNombres.setError("Complete este campo");
            etNombres.requestFocus();
        }else if(dni.isEmpty()){
            etDni.setError("Complete este campo");
            etDni.requestFocus();
        }else if(clave.isEmpty()){
            etClave.setError("Complete este campo");
            etClave.requestFocus();
        }else {
            mostrarPregunta();
        }

    }

    private void mostrarPregunta(){
        //Constructor y configuracion
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Clientes");
        dialogo.setMessage("¿Está seguro de registrar?");
        dialogo.setCancelable(false);

        //Definir Aceptar / cancelar
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //enviar los datos utilizando Volley
                registrarCliente();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //Mostrar diálogo
        dialogo.show();
    }
    private void registrarCliente(){
        String URLUpdate = Utilidades.URL + "cliente.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    reset();
                    etApellidos.requestFocus();
                    Toast.makeText(getApplicationContext(), "guadado correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error comunicacion", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("operacion", "add");
                parametros.put("apellidos", apellidos);
                parametros.put("nombres", nombres);
                parametros.put("dni", dni);
                parametros.put("claveAcceso", clave);
                return parametros;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}