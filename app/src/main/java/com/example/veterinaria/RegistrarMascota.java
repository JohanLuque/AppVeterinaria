package com.example.veterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.security.AppUriAuthenticationPolicy;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class RegistrarMascota extends AppCompatActivity {
    EditText etNombreMascota, etColor;
    RadioButton rbHembra, rbMacho;
    Spinner spRaza, spAnimal;
    RadioGroup rgGenero;
    Button btGuardarMascota;
    private ArrayList<Raza> listaRazas = new ArrayList<>();

     private  ArrayList<Animal> listaAnimal = new ArrayList<>();
    int idcliente, idraza;
    String nombre, color, genero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);
        loadUI();

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            idcliente = parametros.getInt("idcliente");
            Log.i("id", String.valueOf(idcliente));
        }
        else {
            toast("no trae");
        }
        listAnimal();
        spAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    Animal animal = listaAnimal.get(i);
                    int idanimal = animal.getValue();
                    listRaces(idanimal);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Raza raza = listaRazas.get(i);
                idraza =raza.getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btGuardarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });

    }
    private void loadUI(){
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etColor = findViewById(R.id.etColor);

        rbHembra = findViewById(R.id.rbHembra);
        rbMacho = findViewById(R.id.rbMacho);

        spAnimal = findViewById(R.id.spAnimal);
        spRaza = findViewById(R.id.spRaza);
        rgGenero = findViewById(R.id.rgGenero);

        btGuardarMascota = findViewById(R.id.btGuardarMascota);
    }
    private  void listAnimal(){
        String URLUpdate = Utilidades.URL + "mascota.php?operacion=lisAnimal";
        if(listaAnimal.isEmpty()){
            listaAnimal.add(new Animal(0, "seleccione"));
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URLUpdate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try{
                                JSONObject jsonObject = response.getJSONObject(i);
                                int value = jsonObject.getInt("idAnimal");
                                String nombre = jsonObject.getString("nombreAnimal");
                                listaAnimal.add(new Animal(value, nombre));
                            }catch (JSONException e){
                                toast("malo");
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<Animal> adapter = new ArrayAdapter<>(RegistrarMascota.this, android.R.layout.simple_spinner_item, listaAnimal);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spAnimal.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("error");
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    private  void toast(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    private  void validar(){
        nombre = etNombreMascota.getText().toString().trim();
        color = etColor.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombreMascota.setError("Complete este campo");
        } else if (color.isEmpty()) {
            etColor.setError("Complete este campo");
        } else {
            preguntar();
        }

    }
    private void listRaces(int idanimal){
        String URLUpdate = Utilidades.URL+("mascota.php?operacion=readRaces&idAnimal=") + idanimal;
        if(listaRazas.isEmpty()){
            listaRazas.add(new Raza(0, "Seleccione"));
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLUpdate, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int value = jsonObject.getInt("idraza");
                        String item = jsonObject.getString("nombreRaza");
                        listaRazas.add(new Raza(value, item));
                    }catch (JSONException e){
                        Log.d("Error spRazas",e.toString());
                    }
                }
                ArrayAdapter<Raza> adapter = new ArrayAdapter<>(RegistrarMascota.this, android.R.layout.simple_spinner_item, listaRazas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRaza.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("error");
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    private void preguntar(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Guardar datos");
        dialogo.setMessage("¿Desea hacer registrar nuevo?");
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                registrarMascota();
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialogo.show();
    }
    private void registrarMascota(){
        if(rgGenero.getCheckedRadioButtonId() == R.id.rbMacho){
            genero = rbMacho.getText().toString();
        }else if(rgGenero.getCheckedRadioButtonId() == R.id.rbHembra){
            genero = rbHembra.getText().toString();
        }
        String URLUpdate = Utilidades.URL + "mascota.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URLUpdate,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    toast("Correcto");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("Error");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("operacion", "add");
                parametros.put("idCliente", String.valueOf(idcliente));
                parametros.put("idRaza", String.valueOf(idraza));
                parametros.put("nombre", nombre);
                parametros.put("fotografia", "");
                parametros.put("color", color);
                parametros.put("genero", genero);

                return parametros;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}